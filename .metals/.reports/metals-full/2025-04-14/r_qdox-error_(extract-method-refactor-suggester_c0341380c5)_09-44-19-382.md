error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/362.java
text:
```scala
l@@ogger_.error("Gossip error", e);

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.gms;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.net.InetAddress;

import org.apache.cassandra.concurrent.StageManager;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.net.Message;
import org.apache.cassandra.net.MessagingService;
import org.apache.cassandra.service.StorageService;

import org.apache.log4j.Logger;

/**
 * This module is responsible for Gossiping information for the local endpoint. This abstraction
 * maintains the list of live and dead endpoints. Periodically i.e. every 1 second this module
 * chooses a random node and initiates a round of Gossip with it. A round of Gossip involves 3
 * rounds of messaging. For instance if node A wants to initiate a round of Gossip with node B
 * it starts off by sending node B a GossipDigestSynMessage. Node B on receipt of this message
 * sends node A a GossipDigestAckMessage. On receipt of this message node A sends node B a
 * GossipDigestAck2Message which completes a round of Gossip. This module as and when it hears one
 * of the three above mentioned messages updates the Failure Detector with the liveness information.
 */

public class Gossiper implements IFailureDetectionEventListener, IEndPointStateChangePublisher
{
    private class GossipTimerTask extends TimerTask
    {
        public void run()
        {
            try
            {
                //wait on messaging service to start listening
                MessagingService.instance.waitUntilListening();

                synchronized( Gossiper.instance )
                {
                	/* Update the local heartbeat counter. */
                    endPointStateMap_.get(localEndPoint_).getHeartBeatState().updateHeartBeat();
                    List<GossipDigest> gDigests = new ArrayList<GossipDigest>();
                    Gossiper.instance.makeRandomGossipDigest(gDigests);

                    if ( gDigests.size() > 0 )
                    {
                        Message message = makeGossipDigestSynMessage(gDigests);
                        /* Gossip to some random live member */
                        boolean gossipedToSeed = doGossipToLiveMember(message);

                        /* Gossip to some unreachable member with some probability to check if he is back up */
                        doGossipToUnreachableMember(message);

                        /* Gossip to a seed if we did not do so above, or we have seen less nodes
                           than there are seeds.  This prevents partitions where each group of nodes
                           is only gossiping to a subset of the seeds.

                           The most straightforward check would be to check that all the seeds have been
                           verified either as live or unreachable.  To avoid that computation each round,
                           we reason that:

                           either all the live nodes are seeds, in which case non-seeds that come online
                           will introduce themselves to a member of the ring by definition,

                           or there is at least one non-seed node in the list, in which case eventually
                           someone will gossip to it, and then do a gossip to a random seed from the
                           gossipedToSeed check.

                           See CASSANDRA-150 for more exposition. */
                        if (!gossipedToSeed || liveEndpoints_.size() < seeds_.size())
                            doGossipToSeed(message);

                        if (logger_.isTraceEnabled())
                            logger_.trace("Performing status check ...");
                        doStatusCheck();
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    final static int MAX_GOSSIP_PACKET_SIZE = 1428;
    public final static int intervalInMillis_ = 1000;
    private static Logger logger_ = Logger.getLogger(Gossiper.class);
    public static final Gossiper instance = new Gossiper();

    private Timer gossipTimer_;
    private InetAddress localEndPoint_;
    private long aVeryLongTime_;
    private long FatClientTimeout_;
    private Random random_ = new Random();

    /* subscribers for interest in EndPointState change */
    private List<IEndPointStateChangeSubscriber> subscribers_ = new ArrayList<IEndPointStateChangeSubscriber>();

    /* live member set */
    private Set<InetAddress> liveEndpoints_ = new HashSet<InetAddress>();

    /* unreachable member set */
    private Set<InetAddress> unreachableEndpoints_ = new HashSet<InetAddress>();

    /* initial seeds for joining the cluster */
    private Set<InetAddress> seeds_ = new HashSet<InetAddress>();

    /* map where key is the endpoint and value is the state associated with the endpoint */
    Map<InetAddress, EndPointState> endPointStateMap_ = new Hashtable<InetAddress, EndPointState>();

    /* map where key is endpoint and value is timestamp when this endpoint was removed from
     * gossip. We will ignore any gossip regarding these endpoints for Streaming.RING_DELAY time
     * after removal to prevent nodes from falsely reincarnating during the time when removal
     * gossip gets propagated to all nodes */
    Map<InetAddress, Long> justRemovedEndPoints_ = new Hashtable<InetAddress, Long>();

    private Gossiper()
    {
        gossipTimer_ = new Timer(false);
        // 3 days
        aVeryLongTime_ = 259200 * 1000;
        // 1 hour
        FatClientTimeout_ = 60 * 60 * 1000;
        /* register with the Failure Detector for receiving Failure detector events */
        FailureDetector.instance.registerFailureDetectionEventListener(this);
    }

    /** Register with the Gossiper for EndPointState notifications */
    public synchronized void register(IEndPointStateChangeSubscriber subscriber)
    {
        subscribers_.add(subscriber);
    }

    public synchronized void unregister(IEndPointStateChangeSubscriber subscriber)
    {
        subscribers_.remove(subscriber);
    }

    public Set<InetAddress> getLiveMembers()
    {
        Set<InetAddress> liveMbrs = new HashSet<InetAddress>(liveEndpoints_);
        liveMbrs.add(localEndPoint_);
        return liveMbrs;
    }

    public Set<InetAddress> getUnreachableMembers()
    {
        return new HashSet<InetAddress>(unreachableEndpoints_);
    }

    /**
     * This method is part of IFailureDetectionEventListener interface. This is invoked
     * by the Failure Detector when it convicts an end point.
     *
     * param @ endpoint end point that is convicted.
    */
    public void convict(InetAddress endpoint)
    {
        EndPointState epState = endPointStateMap_.get(endpoint);
        if (epState.isAlive())
        {
            logger_.info("InetAddress " + endpoint + " is now dead.");
            isAlive(endpoint, epState, false);
        }
    }

    int getMaxEndPointStateVersion(EndPointState epState)
    {
        List<Integer> versions = new ArrayList<Integer>();
        versions.add( epState.getHeartBeatState().getHeartBeatVersion() );
        Map<String, ApplicationState> appStateMap = epState.getApplicationStateMap();

        for (ApplicationState value : appStateMap.values())
        {
            int stateVersion = value.getStateVersion();
            versions.add( stateVersion );
        }

        /* sort to get the max version to build GossipDigest for this endpoint */
        Collections.sort(versions);
        int maxVersion = versions.get(versions.size() - 1);
        versions.clear();
        return maxVersion;
    }

    /**
     * Removes the endpoint from unreachable endpoint set
     *
     * @param endpoint endpoint to be removed from the current membership.
    */
    void evictFromMembership(InetAddress endpoint)
    {
        unreachableEndpoints_.remove(endpoint);
    }

    /**
     * Removes the endpoint completely from Gossip
     */
    public void removeEndPoint(InetAddress endpoint)
    {
        liveEndpoints_.remove(endpoint);
        unreachableEndpoints_.remove(endpoint);
        endPointStateMap_.remove(endpoint);
        FailureDetector.instance.remove(endpoint);
        justRemovedEndPoints_.put(endpoint, System.currentTimeMillis());
    }

    /**
     * No locking required since it is called from a method that already
     * has acquired a lock. The gossip digest is built based on randomization
     * rather than just looping through the collection of live endpoints.
     *
     * @param gDigests list of Gossip Digests.
    */
    void makeRandomGossipDigest(List<GossipDigest> gDigests)
    {
        /* Add the local endpoint state */
        EndPointState epState = endPointStateMap_.get(localEndPoint_);
        int generation = epState.getHeartBeatState().getGeneration();
        int maxVersion = getMaxEndPointStateVersion(epState);
        gDigests.add( new GossipDigest(localEndPoint_, generation, maxVersion) );

        List<InetAddress> endpoints = new ArrayList<InetAddress>(endPointStateMap_.keySet());
        Collections.shuffle(endpoints, random_);
        for (InetAddress endPoint : endpoints)
        {
            epState = endPointStateMap_.get(endPoint);
            if (epState != null)
            {
                generation = epState.getHeartBeatState().getGeneration();
                maxVersion = getMaxEndPointStateVersion(epState);
                gDigests.add(new GossipDigest(endPoint, generation, maxVersion));
            }
            else
            {
            	gDigests.add(new GossipDigest(endPoint, 0, 0));
            }
        }

        /* FOR DEBUG ONLY - remove later */
        StringBuilder sb = new StringBuilder();
        for ( GossipDigest gDigest : gDigests )
        {
            sb.append(gDigest);
            sb.append(" ");
        }
        if (logger_.isTraceEnabled())
            logger_.trace("Gossip Digests are : " + sb.toString());
    }

    public boolean isKnownEndpoint(InetAddress endpoint)
    {
        return endPointStateMap_.containsKey(endpoint);
    }

    public int getCurrentGenerationNumber(InetAddress endpoint)
    {
    	return endPointStateMap_.get(endpoint).getHeartBeatState().getGeneration();
    }

    Message makeGossipDigestSynMessage(List<GossipDigest> gDigests) throws IOException
    {
        GossipDigestSynMessage gDigestMessage = new GossipDigestSynMessage(DatabaseDescriptor.getClusterName(), gDigests);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(Gossiper.MAX_GOSSIP_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream( bos );
        GossipDigestSynMessage.serializer().serialize(gDigestMessage, dos);
        return new Message(localEndPoint_, StageManager.GOSSIP_STAGE, StorageService.Verb.GOSSIP_DIGEST_SYN, bos.toByteArray());
    }

    Message makeGossipDigestAckMessage(GossipDigestAckMessage gDigestAckMessage) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(Gossiper.MAX_GOSSIP_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(bos);
        GossipDigestAckMessage.serializer().serialize(gDigestAckMessage, dos);
        if (logger_.isTraceEnabled())
            logger_.trace("@@@@ Size of GossipDigestAckMessage is " + bos.toByteArray().length);
        return new Message(localEndPoint_, StageManager.GOSSIP_STAGE, StorageService.Verb.GOSSIP_DIGEST_ACK, bos.toByteArray());
    }

    Message makeGossipDigestAck2Message(GossipDigestAck2Message gDigestAck2Message) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(Gossiper.MAX_GOSSIP_PACKET_SIZE);
        DataOutputStream dos = new DataOutputStream(bos);
        GossipDigestAck2Message.serializer().serialize(gDigestAck2Message, dos);
        return new Message(localEndPoint_, StageManager.GOSSIP_STAGE, StorageService.Verb.GOSSIP_DIGEST_ACK2, bos.toByteArray());
    }

    /**
     * Returns true if the chosen target was also a seed. False otherwise
     *
     *  @param message message to sent
     *  @param epSet a set of endpoint from which a random endpoint is chosen.
     *  @return true if the chosen endpoint is also a seed.
     */
    boolean sendGossip(Message message, Set<InetAddress> epSet)
    {
        int size = epSet.size();
        /* Generate a random number from 0 -> size */
        List<InetAddress> liveEndPoints = new ArrayList<InetAddress>(epSet);
        int index = (size == 1) ? 0 : random_.nextInt(size);
        InetAddress to = liveEndPoints.get(index);
        if (logger_.isTraceEnabled())
            logger_.trace("Sending a GossipDigestSynMessage to " + to + " ...");
        MessagingService.instance.sendOneWay(message, to);
        return seeds_.contains(to);
    }

    /* Sends a Gossip message to a live member and returns true if the recipient was a seed */
    boolean doGossipToLiveMember(Message message)
    {
        int size = liveEndpoints_.size();
        if ( size == 0 )
            return false;
        // return sendGossipToLiveNode(message);
        /* Use this for a cluster size >= 30 */
        return sendGossip(message, liveEndpoints_);
    }

    /* Sends a Gossip message to an unreachable member */
    void doGossipToUnreachableMember(Message message)
    {
        double liveEndPoints = liveEndpoints_.size();
        double unreachableEndPoints = unreachableEndpoints_.size();
        if ( unreachableEndPoints > 0 )
        {
            /* based on some probability */
            double prob = unreachableEndPoints / (liveEndPoints + 1);
            double randDbl = random_.nextDouble();
            if ( randDbl < prob )
                sendGossip(message, unreachableEndpoints_);
        }
    }

    /* Gossip to a seed for facilitating partition healing */
    void doGossipToSeed(Message message)
    {
        int size = seeds_.size();
        if ( size > 0 )
        {
            if ( size == 1 && seeds_.contains(localEndPoint_) )
            {
                return;
            }

            if ( liveEndpoints_.size() == 0 )
            {
                sendGossip(message, seeds_);
            }
            else
            {
                /* Gossip with the seed with some probability. */
                double probability = seeds_.size() / (double)( liveEndpoints_.size() + unreachableEndpoints_.size() );
                double randDbl = random_.nextDouble();
                if ( randDbl <= probability )
                    sendGossip(message, seeds_);
            }
        }
    }

    void doStatusCheck()
    {
        long now = System.currentTimeMillis();

        Set<InetAddress> eps = endPointStateMap_.keySet();
        for ( InetAddress endpoint : eps )
        {
            if ( endpoint.equals(localEndPoint_) )
                continue;

            FailureDetector.instance.interpret(endpoint);
            EndPointState epState = endPointStateMap_.get(endpoint);
            if ( epState != null )
            {
                long duration = now - epState.getUpdateTimestamp();

                // check if this is a fat client. fat clients are removed automatically from
                // gosip after FatClientTimeout
                if (!epState.getHasToken() && !epState.isAlive() && (duration > FatClientTimeout_))
                {
                    if (StorageService.instance.getTokenMetadata().isMember(endpoint))
                        epState.setHasToken(true);
                    else
                    {
                        logger_.info("FatClient " + endpoint + " has been silent for " + FatClientTimeout_ + "ms, removing from gossip");
                        removeEndPoint(endpoint);
                    }
                }

                if ( !epState.isAlive() && (duration > aVeryLongTime_) )
                {
                    evictFromMembership(endpoint);
                }
            }

            if (!justRemovedEndPoints_.isEmpty())
            {
                Hashtable<InetAddress, Long> copy = new Hashtable<InetAddress, Long>(justRemovedEndPoints_);
                for (Map.Entry<InetAddress, Long> entry : copy.entrySet())
                {
                    if ((now - entry.getValue()) > StorageService.RING_DELAY)
                    {
                        if (logger_.isDebugEnabled())
                            logger_.debug(StorageService.RING_DELAY + " elapsed, " + entry.getKey() + " gossip quarantine over");
                        justRemovedEndPoints_.remove(entry.getKey());
                    }
                }
            }
        }
    }

    EndPointState getEndPointStateForEndPoint(InetAddress ep)
    {
        return endPointStateMap_.get(ep);
    }

    synchronized EndPointState getStateForVersionBiggerThan(InetAddress forEndpoint, int version)
    {
        if (logger_.isTraceEnabled())
            logger_.trace("Scanning for state greater than " + version + " for " + forEndpoint);
        EndPointState epState = endPointStateMap_.get(forEndpoint);
        EndPointState reqdEndPointState = null;

        if ( epState != null )
        {
            /*
             * Here we try to include the Heart Beat state only if it is
             * greater than the version passed in. It might happen that
             * the heart beat version maybe lesser than the version passed
             * in and some application state has a version that is greater
             * than the version passed in. In this case we also send the old
             * heart beat and throw it away on the receiver if it is redundant.
            */
            int localHbVersion = epState.getHeartBeatState().getHeartBeatVersion();
            if ( localHbVersion > version )
            {
                reqdEndPointState = new EndPointState(epState.getHeartBeatState());
            }
            Map<String, ApplicationState> appStateMap = epState.getApplicationStateMap();
            /* Accumulate all application states whose versions are greater than "version" variable */
            for (Entry<String, ApplicationState> entry : appStateMap.entrySet())
            {
                ApplicationState appState = entry.getValue();
                if ( appState.getStateVersion() > version )
                {
                    if ( reqdEndPointState == null )
                    {
                        reqdEndPointState = new EndPointState(epState.getHeartBeatState());
                    }
                    final String key = entry.getKey();
                    if (logger_.isTraceEnabled())
                        logger_.trace("Adding state " + key + ": " + appState.getValue());
                    reqdEndPointState.addApplicationState(key, appState);
                }
            }
        }
        return reqdEndPointState;
    }
    
    /** determine which endpoint started up earlier */
    public int compareEndpointStartup(InetAddress addr1, InetAddress addr2)
    {
        EndPointState ep1 = getEndPointStateForEndPoint(addr1);
        EndPointState ep2 = getEndPointStateForEndPoint(addr2);
        assert ep1 != null && ep2 != null;
        return ep1.getHeartBeatState().getGeneration() - ep2.getHeartBeatState().getGeneration();
    }    

    /*
     * This method is called only from the GossiperJoinVerbHandler. This happens
     * when a new node coming up multicasts the JoinMessage. Here we need
     * to add the endPoint to the list of live endpoints.
    */
    synchronized void join(InetAddress from)
    {
        if ( !from.equals( localEndPoint_ ) )
        {
            /* Mark this endpoint as "live" */
        	liveEndpoints_.add(from);
            unreachableEndpoints_.remove(from);
        }
    }

    void notifyFailureDetector(List<GossipDigest> gDigests)
    {
        IFailureDetector fd = FailureDetector.instance;
        for ( GossipDigest gDigest : gDigests )
        {
            EndPointState localEndPointState = endPointStateMap_.get(gDigest.endPoint_);
            /*
             * If the local endpoint state exists then report to the FD only
             * if the versions workout.
            */
            if ( localEndPointState != null )
            {
                int localGeneration = endPointStateMap_.get(gDigest.endPoint_).getHeartBeatState().generation_;
                int remoteGeneration = gDigest.generation_;
                if ( remoteGeneration > localGeneration )
                {
                    fd.report(gDigest.endPoint_);
                    continue;
                }

                if ( remoteGeneration == localGeneration )
                {
                    int localVersion = getMaxEndPointStateVersion(localEndPointState);
                    //int localVersion = endPointStateMap_.get(gDigest.endPoint_).getHeartBeatState().getHeartBeatVersion();
                    int remoteVersion = gDigest.maxVersion_;
                    if ( remoteVersion > localVersion )
                    {
                        fd.report(gDigest.endPoint_);
                    }
                }
            }
        }
    }

    void notifyFailureDetector(Map<InetAddress, EndPointState> remoteEpStateMap)
    {
        IFailureDetector fd = FailureDetector.instance;
        for (Entry<InetAddress, EndPointState> entry : remoteEpStateMap.entrySet())
        {
            InetAddress endpoint = entry.getKey();
            EndPointState remoteEndPointState = entry.getValue();
            EndPointState localEndPointState = endPointStateMap_.get(endpoint);
            /*
             * If the local endpoint state exists then report to the FD only
             * if the versions workout.
            */
            if ( localEndPointState != null )
            {
                int localGeneration = localEndPointState.getHeartBeatState().generation_;
                int remoteGeneration = remoteEndPointState.getHeartBeatState().generation_;
                if ( remoteGeneration > localGeneration )
                {
                    fd.report(endpoint);
                    continue;
                }

                if ( remoteGeneration == localGeneration )
                {
                    int localVersion = getMaxEndPointStateVersion(localEndPointState);
                    //int localVersion = localEndPointState.getHeartBeatState().getHeartBeatVersion();
                    int remoteVersion = remoteEndPointState.getHeartBeatState().getHeartBeatVersion();
                    if ( remoteVersion > localVersion )
                    {
                        fd.report(endpoint);
                    }
                }
            }
        }
    }

    void markAlive(InetAddress addr, EndPointState localState)
    {
        if (logger_.isTraceEnabled())
            logger_.trace("marking as alive " + addr);
        if ( !localState.isAlive() )
        {
            isAlive(addr, localState, true);
            logger_.info("InetAddress " + addr + " is now UP");
        }
    }

    private void handleNewJoin(InetAddress ep, EndPointState epState)
    {
        if (justRemovedEndPoints_.containsKey(ep))
            return;
    	logger_.info("Node " + ep + " is now part of the cluster");
        handleMajorStateChange(ep, epState, false);
    }

    private void handleGenerationChange(InetAddress ep, EndPointState epState)
    {
        logger_.info("Node " + ep + " has restarted, now UP again");
        handleMajorStateChange(ep, epState, true);
    }

    /**
     * This method is called whenever there is a "big" change in ep state (either a previously
     * unknown node or a generation change for a known node). If the node is new, it will be
     * initially marked as dead. It will be marked alive as soon as another piece of gossip
     * arrives. On the other hand if the node is already known (generation change), we will
     * immediately mark it alive.
     *
     * @param ep endpoint
     * @param epState EndPointState for the endpoint
     * @param isKnownNode is this node familiar to us already (present in endPointStateMap)
     */
    private void handleMajorStateChange(InetAddress ep, EndPointState epState, boolean isKnownNode)
    {
        endPointStateMap_.put(ep, epState);
        isAlive(ep, epState, isKnownNode);
        for (IEndPointStateChangeSubscriber subscriber : subscribers_)
            subscriber.onJoin(ep, epState);
    }

    synchronized void applyStateLocally(Map<InetAddress, EndPointState> epStateMap)
    {
        for (Entry<InetAddress, EndPointState> entry : epStateMap.entrySet())
        {
            InetAddress ep = entry.getKey();
            if ( ep.equals( localEndPoint_ ) )
                continue;

            EndPointState localEpStatePtr = endPointStateMap_.get(ep);
            EndPointState remoteState = entry.getValue();
            /*
                If state does not exist just add it. If it does then add it only if the version
                of the remote copy is greater than the local copy.
            */
            if ( localEpStatePtr != null )
            {
            	int localGeneration = localEpStatePtr.getHeartBeatState().getGeneration();
            	int remoteGeneration = remoteState.getHeartBeatState().getGeneration();

            	if (remoteGeneration > localGeneration)
            	{
                    handleGenerationChange(ep, remoteState);
            	}
            	else if ( remoteGeneration == localGeneration )
            	{
	                /* manage the membership state */
	                int localMaxVersion = getMaxEndPointStateVersion(localEpStatePtr);
	                int remoteMaxVersion = getMaxEndPointStateVersion(remoteState);
	                if ( remoteMaxVersion > localMaxVersion )
	                {
	                    markAlive(ep, localEpStatePtr);
	                    applyHeartBeatStateLocally(ep, localEpStatePtr, remoteState);
	                    /* apply ApplicationState */
	                    applyApplicationStateLocally(ep, localEpStatePtr, remoteState);
	                }
            	}
            }
            else
            {
            	handleNewJoin(ep, remoteState);
            }
        }
    }

    void applyHeartBeatStateLocally(InetAddress addr, EndPointState localState, EndPointState remoteState)
    {
        HeartBeatState localHbState = localState.getHeartBeatState();
        HeartBeatState remoteHbState = remoteState.getHeartBeatState();

        if ( remoteHbState.getGeneration() > localHbState.getGeneration() )
        {
            localState.setHeartBeatState(remoteHbState);
        }
        if ( localHbState.getGeneration() == remoteHbState.getGeneration() )
        {
            if ( remoteHbState.getHeartBeatVersion() > localHbState.getHeartBeatVersion() )
            {
                int oldVersion = localHbState.getHeartBeatVersion();
                localState.setHeartBeatState(remoteHbState);
                if (logger_.isTraceEnabled())
                    logger_.trace("Updating heartbeat state version to " + localState.getHeartBeatState().getHeartBeatVersion() + " from " + oldVersion + " for " + addr + " ...");
            }
        }
    }

    void applyApplicationStateLocally(InetAddress addr, EndPointState localStatePtr, EndPointState remoteStatePtr)
    {
        Map<String, ApplicationState> localAppStateMap = localStatePtr.getApplicationStateMap();

        for (Map.Entry<String,ApplicationState> remoteEntry : remoteStatePtr.getSortedApplicationStates())
        {
            String remoteKey = remoteEntry.getKey();
            ApplicationState remoteAppState = remoteEntry.getValue();
            ApplicationState localAppState = localAppStateMap.get(remoteKey);

            /* If state doesn't exist locally for this key then just apply it */
            if ( localAppState == null )
            {
                localStatePtr.addApplicationState(remoteKey, remoteAppState);
                doNotifications(addr, remoteKey, remoteAppState);
                continue;
            }

            int remoteGeneration = remoteStatePtr.getHeartBeatState().getGeneration();
            int localGeneration = localStatePtr.getHeartBeatState().getGeneration();
            assert remoteGeneration >= localGeneration; // SystemTable makes sure we never generate a smaller generation on start

            /* If the remoteGeneration is greater than localGeneration then apply state blindly */
            if ( remoteGeneration > localGeneration )
            {
                localStatePtr.addApplicationState(remoteKey, remoteAppState);
                doNotifications(addr, remoteKey, remoteAppState);
                continue;
            }

            /* If the generations are the same then apply state if the remote version is greater than local version. */
            if ( remoteGeneration == localGeneration )
            {
                int remoteVersion = remoteAppState.getStateVersion();
                int localVersion = localAppState.getStateVersion();

                if ( remoteVersion > localVersion )
                {
                    localStatePtr.addApplicationState(remoteKey, remoteAppState);
                    doNotifications(addr, remoteKey, remoteAppState);
                }
            }
        }
    }

    void doNotifications(InetAddress addr, String stateName, ApplicationState state)
    {
        for (IEndPointStateChangeSubscriber subscriber : subscribers_)
        {
            subscriber.onChange(addr, stateName, state);
        }
    }

    synchronized void isAlive(InetAddress addr, EndPointState epState, boolean value)
    {
        epState.isAlive(value);
        if (value)
        {
            liveEndpoints_.add(addr);
            unreachableEndpoints_.remove(addr);
            for (IEndPointStateChangeSubscriber subscriber : subscribers_)
                subscriber.onAlive(addr, epState);
        }
        else
        {
            liveEndpoints_.remove(addr);
            unreachableEndpoints_.add(addr);
            for (IEndPointStateChangeSubscriber subscriber : subscribers_)
                subscriber.onDead(addr, epState);
        }
        if (epState.isAGossiper())
            return;
        epState.isAGossiper(true);
    }

    /* Request all the state for the endpoint in the gDigest */
    void requestAll(GossipDigest gDigest, List<GossipDigest> deltaGossipDigestList, int remoteGeneration)
    {
        /* We are here since we have no data for this endpoint locally so request everthing. */
        deltaGossipDigestList.add( new GossipDigest(gDigest.getEndPoint(), remoteGeneration, 0) );
    }

    /* Send all the data with version greater than maxRemoteVersion */
    void sendAll(GossipDigest gDigest, Map<InetAddress, EndPointState> deltaEpStateMap, int maxRemoteVersion)
    {
        EndPointState localEpStatePtr = getStateForVersionBiggerThan(gDigest.getEndPoint(), maxRemoteVersion) ;
        if ( localEpStatePtr != null )
            deltaEpStateMap.put(gDigest.getEndPoint(), localEpStatePtr);
    }

    /*
        This method is used to figure the state that the Gossiper has but Gossipee doesn't. The delta digests
        and the delta state are built up.
    */
    synchronized void examineGossiper(List<GossipDigest> gDigestList, List<GossipDigest> deltaGossipDigestList, Map<InetAddress, EndPointState> deltaEpStateMap)
    {
        for ( GossipDigest gDigest : gDigestList )
        {
            int remoteGeneration = gDigest.getGeneration();
            int maxRemoteVersion = gDigest.getMaxVersion();
            /* Get state associated with the end point in digest */
            EndPointState epStatePtr = endPointStateMap_.get(gDigest.getEndPoint());
            /*
                Here we need to fire a GossipDigestAckMessage. If we have some data associated with this endpoint locally
                then we follow the "if" path of the logic. If we have absolutely nothing for this endpoint we need to
                request all the data for this endpoint.
            */
            if ( epStatePtr != null )
            {
                int localGeneration = epStatePtr.getHeartBeatState().getGeneration();
                /* get the max version of all keys in the state associated with this endpoint */
                int maxLocalVersion = getMaxEndPointStateVersion(epStatePtr);
                if ( remoteGeneration == localGeneration && maxRemoteVersion == maxLocalVersion )
                    continue;

                if ( remoteGeneration > localGeneration )
                {
                    /* we request everything from the gossiper */
                    requestAll(gDigest, deltaGossipDigestList, remoteGeneration);
                }
                if ( remoteGeneration < localGeneration )
                {
                    /* send all data with generation = localgeneration and version > 0 */
                    sendAll(gDigest, deltaEpStateMap, 0);
                }
                if ( remoteGeneration == localGeneration )
                {
                    /*
                        If the max remote version is greater then we request the remote endpoint send us all the data
                        for this endpoint with version greater than the max version number we have locally for this
                        endpoint.
                        If the max remote version is lesser, then we send all the data we have locally for this endpoint
                        with version greater than the max remote version.
                    */
                    if ( maxRemoteVersion > maxLocalVersion )
                    {
                        deltaGossipDigestList.add( new GossipDigest(gDigest.getEndPoint(), remoteGeneration, maxLocalVersion) );
                    }
                    if ( maxRemoteVersion < maxLocalVersion )
                    {
                        /* send all data with generation = localgeneration and version > maxRemoteVersion */
                        sendAll(gDigest, deltaEpStateMap, maxRemoteVersion);
                    }
                }
            }
            else
            {
                /* We are here since we have no data for this endpoint locally so request everything. */
                requestAll(gDigest, deltaGossipDigestList, remoteGeneration);
            }
        }
    }

    /**
     * Start the gossiper with the generation # retrieved from the System
     * table
     */
    public void start(InetAddress localEndPoint, int generationNbr)
    {
        localEndPoint_ = localEndPoint;
        /* Get the seeds from the config and initialize them. */
        Set<InetAddress> seedHosts = DatabaseDescriptor.getSeeds();
        for (InetAddress seed : seedHosts)
        {
            if (seed.equals(localEndPoint))
                continue;
            seeds_.add(seed);
        }

        /* initialize the heartbeat state for this localEndPoint */
        EndPointState localState = endPointStateMap_.get(localEndPoint_);
        if ( localState == null )
        {
            HeartBeatState hbState = new HeartBeatState(generationNbr);
            localState = new EndPointState(hbState);
            localState.isAlive(true);
            localState.isAGossiper(true);
            endPointStateMap_.put(localEndPoint_, localState);
        }

        /* starts a timer thread */
        gossipTimer_.schedule( new GossipTimerTask(), Gossiper.intervalInMillis_, Gossiper.intervalInMillis_);
    }

    public synchronized void addLocalApplicationState(String key, ApplicationState appState)
    {
        assert !StorageService.instance.isClientMode();
        EndPointState epState = endPointStateMap_.get(localEndPoint_);
        assert epState != null;
        epState.addApplicationState(key, appState);
    }

    public void stop()
    {
        gossipTimer_.cancel();
        gossipTimer_ = new Timer(false); // makes the Gossiper reentrant.
    }
}

```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/362.java