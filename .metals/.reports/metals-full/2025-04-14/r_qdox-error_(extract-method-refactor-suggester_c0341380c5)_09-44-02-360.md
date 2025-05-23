error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3666.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3666.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3666.java
text:
```scala
t@@his.stopfitness = stopFitness;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.optimization.direct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.exception.MultiDimensionMismatchException;
import org.apache.commons.math.exception.NoDataException;
import org.apache.commons.math.exception.NotPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.TooManyEvaluationsException;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.EigenDecomposition;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MultivariateRealOptimizer;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.random.MersenneTwister;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.MathArrays;

/**
 * <p>An implementation of the active Covariance Matrix Adaptation Evolution Strategy (CMA-ES)
 * for non-linear, non-convex, non-smooth, global function minimization.
 * The CMA-Evolution Strategy (CMA-ES) is a reliable stochastic optimization method
 * which should be applied if derivative-based methods, e.g. quasi-Newton BFGS or
 * conjugate gradient, fail due to a rugged search landscape (e.g. noise, local
 * optima, outlier, etc.) of the objective function. Like a
 * quasi-Newton method, the CMA-ES learns and applies a variable metric
 * on the underlying search space. Unlike a quasi-Newton method, the
 * CMA-ES neither estimates nor uses gradients, making it considerably more
 * reliable in terms of finding a good, or even close to optimal, solution.</p>
 *
 * <p>In general, on smooth objective functions the CMA-ES is roughly ten times
 * slower than BFGS (counting objective function evaluations, no gradients provided).
 * For up to <math>N=10</math> variables also the derivative-free simplex
 * direct search method (Nelder and Mead) can be faster, but it is
 * far less reliable than CMA-ES.</p>
 *
 * <p>The CMA-ES is particularly well suited for non-separable
 * and/or badly conditioned problems. To observe the advantage of CMA compared
 * to a conventional evolution strategy, it will usually take about
 * <math>30 N</math> function evaluations. On difficult problems the complete
 * optimization (a single run) is expected to take <em>roughly</em> between
 * <math>30 N</math> and <math>300 N<sup>2</sup></math>
 * function evaluations.</p>
 *
 * <p>This implementation is translated and adapted from the Matlab version
 * of the CMA-ES algorithm as implemented in module {@code cmaes.m} version 3.51.</p>
 *
 * For more information, please refer to the following links:
 * <ul>
 *  <li><a href="http://www.lri.fr/~hansen/cmaes.m">Matlab code</a></li>
 *  <li><a href="http://www.lri.fr/~hansen/cmaesintro.html">Introduction to CMA-ES</a></li>
 *  <li><a href="http://en.wikipedia.org/wiki/CMA-ES">Wikipedia</a></li>
 * </ul>
 *
 * @version $Id$
 * @since 3.0
 */

public class CMAESOptimizer
    extends BaseAbstractScalarOptimizer<MultivariateRealFunction>
    implements MultivariateRealOptimizer {
    /** Default value for {@link #checkFeasableCount}: {@value}. */
    public static final int DEFAULT_CHECKFEASABLECOUNT = 0;
    /** Default value for {@link #stopfitness}: {@value}. */
    public static final double DEFAULT_STOPFITNESS = 0;
    /** Default value for {@link #isActiveCMA}: {@value}. */
    public static final boolean DEFAULT_ISACTIVECMA = true;
    /** Default value for {@link #maxIterations}: {@value}. */
    public static final int DEFAULT_MAXITERATIONS = 30000;
    /** Default value for {@link #diagonalOnly}: {@value}. */
    public static final int DEFAULT_DIAGONALONLY = 0;
    /** Default value for {@link #random}. */
    public static final RandomGenerator DEFAULT_RANDOMGENERATOR = new MersenneTwister();

    // global search parameters
    /**
     * Population size, offspring number. The primary strategy parameter to play
     * with, which can be increased from its default value. Increasing the
     * population size improves global search properties in exchange to speed.
     * Speed decreases, as a rule, at most linearly with increasing population
     * size. It is advisable to begin with the default small population size.
     */
    private int lambda; // population size
    /**
     * Covariance update mechanism, default is active CMA. isActiveCMA = true
     * turns on "active CMA" with a negative update of the covariance matrix and
     * checks for positive definiteness. OPTS.CMA.active = 2 does not check for
     * pos. def. and is numerically faster. Active CMA usually speeds up the
     * adaptation.
     */
    private boolean isActiveCMA;
    /**
     * Determines how often a new random offspring is generated in case it is
     * not feasible / beyond the defined limits, default is 0. Only relevant if
     * boundaries != null.
     */
    private int checkFeasableCount;
    /**
     * Lower and upper boundaries of the objective variables. boundaries == null
     * means no boundaries.
     */
    private double[][] boundaries;
    /**
     * Individual sigma values - initial search volume. inputSigma determines
     * the initial coordinate wise standard deviations for the search. Setting
     * SIGMA one third of the initial search region is appropriate.
     */
    private double[] inputSigma;
    /** Number of objective variables/problem dimension */
    private int dimension;
    /**
     * Defines the number of initial iterations, where the covariance matrix
     * remains diagonal and the algorithm has internally linear time complexity.
     * diagonalOnly = 1 means keeping the covariance matrix always diagonal and
     * this setting also exhibits linear space complexity. This can be
     * particularly useful for dimension > 100.
     * @see <a href="http://hal.archives-ouvertes.fr/inria-00287367/en">A Simple Modification in CMA-ES</a>
     */
    private int diagonalOnly = 0;
    /** Number of objective variables/problem dimension */
    private boolean isMinimize = true;
    /** Indicates whether statistic data is collected. */
    private boolean generateStatistics = false;

    // termination criteria
    /** Maximal number of iterations allowed. */
    private int maxIterations;
    /** Limit for fitness value. */
    private double stopfitness;
    /** Stop if x-changes larger stopTolUpX. */
    private double stopTolUpX;
    /** Stop if x-change smaller stopTolX. */
    private double stopTolX;
    /** Stop if fun-changes smaller stopTolFun. */
    private double stopTolFun;
    /** Stop if back fun-changes smaller stopTolHistFun. */
    private double stopTolHistFun;

    // selection strategy parameters
    /** Number of parents/points for recombination. */
    private int mu; //
    /** log(mu + 0.5), stored for efficiency. */
    private double logMu2;
    /** Array for weighted recombination. */
    private RealMatrix weights;
    /** Variance-effectiveness of sum w_i x_i. */
    private double mueff; //

    // dynamic strategy parameters and constants
    /** Overall standard deviation - search volume. */
    private double sigma;
    /** Cumulation constant. */
    private double cc;
    /** Cumulation constant for step-size. */
    private double cs;
    /** Damping for step-size. */
    private double damps;
    /** Learning rate for rank-one update. */
    private double ccov1;
    /** Learning rate for rank-mu update' */
    private double ccovmu;
    /** Expectation of ||N(0,I)|| == norm(randn(N,1)). */
    private double chiN;
    /** Learning rate for rank-one update - diagonalOnly */
    private double ccov1Sep;
    /** Learning rate for rank-mu update - diagonalOnly */
    private double ccovmuSep;

    // CMA internal values - updated each generation
    /** Objective variables. */
    private RealMatrix xmean;
    /** Evolution path. */
    private RealMatrix pc;
    /** Evolution path for sigma. */
    private RealMatrix ps;
    /** Norm of ps, stored for efficiency. */
    private double normps;
    /** Coordinate system. */
    private RealMatrix B;
    /** Scaling. */
    private RealMatrix D;
    /** B*D, stored for efficiency. */
    private RealMatrix BD;
    /** Diagonal of sqrt(D), stored for efficiency. */
    private RealMatrix diagD;
    /** Covariance matrix. */
    private RealMatrix C;
    /** Diagonal of C, used for diagonalOnly. */
    private RealMatrix diagC;
    /** Number of iterations already performed. */
    private int iterations;

    /** History queue of best values. */
    private double[] fitnessHistory;
    /** Size of history queue of best values. */
    private int historySize;

    /** Random generator. */
    private RandomGenerator random;

    /** History of sigma values. */
    private List<Double> statisticsSigmaHistory = new ArrayList<Double>();
    /** History of mean matrix. */
    private List<RealMatrix> statisticsMeanHistory = new ArrayList<RealMatrix>();
    /** History of fitness values. */
    private List<Double> statisticsFitnessHistory = new ArrayList<Double>();
    /** History of D matrix. */
    private List<RealMatrix> statisticsDHistory = new ArrayList<RealMatrix>();

    /**
     * Default constructor, uses default parameters
     */
    public CMAESOptimizer() {
        this(0);
    }

    /**
     * @param lambda Population size.
     */
    public CMAESOptimizer(int lambda) {
        this(lambda, null, null, DEFAULT_MAXITERATIONS, DEFAULT_STOPFITNESS,
             DEFAULT_ISACTIVECMA, DEFAULT_DIAGONALONLY,
             DEFAULT_CHECKFEASABLECOUNT, DEFAULT_RANDOMGENERATOR, false);
    }

    /**
     * @param lambda Population size.
     * @param inputSigma Initial search volume; sigma of offspring objective variables.
     * @param boundaries Boundaries for objective variables.
     */
    public CMAESOptimizer(int lambda, double[] inputSigma,
                          double[][] boundaries) {
        this(lambda, inputSigma, boundaries, DEFAULT_MAXITERATIONS, DEFAULT_STOPFITNESS,
             DEFAULT_ISACTIVECMA, DEFAULT_DIAGONALONLY,
             DEFAULT_CHECKFEASABLECOUNT, DEFAULT_RANDOMGENERATOR, false);
    }

    /**
     * @param lambda Population size.
     * @param inputSigma Initial search volume; sigma of offspring objective variables.
     * @param boundaries Boundaries for objective variables.
     * @param maxIterations Maximal number of iterations.
     * @param stopFitness Whether to stop if objective function value is smaller than
     * {@code stopFitness}.
     * @param isActiveCMA Chooses the covariance matrix update method.
     * @param diagonalOnly Number of initial iterations, where the covariance matrix
     * remains diagonal.
     * @param checkFeasableCount Determines how often new random objective variables are
     * generated in case they are out of bounds.
     * @param random Random generator.
     * @param generateStatistics Whether statistic data is collected.
     */
    public CMAESOptimizer(int lambda, double[] inputSigma,
                          double[][] boundaries, int maxIterations, double stopFitness,
                          boolean isActiveCMA, int diagonalOnly, int checkFeasableCount,
                          RandomGenerator random, boolean generateStatistics) {
        this.lambda = lambda;
        this.inputSigma = inputSigma == null ? null : (double[]) inputSigma.clone();
        if (boundaries == null) {
            this.boundaries = null;
        } else {
            final int len = boundaries.length;
            this.boundaries = new double[len][];
            for (int i = 0; i < len; i++) {
                this.boundaries[i] =
                    boundaries[i] == null ? null : (double[]) boundaries[i].clone();
            }
        }
        this.maxIterations = maxIterations;
        this.stopfitness = stopfitness;
        this.isActiveCMA = isActiveCMA;
        this.diagonalOnly = diagonalOnly;
        this.checkFeasableCount = checkFeasableCount;
        this.random = random;
        this.generateStatistics = generateStatistics;
    }

    /**
     * @return History of sigma values.
     */
    public List<Double> getStatisticsSigmaHistory() {
        return statisticsSigmaHistory;
    }

    /**
     * @return History of mean matrix.
     */
    public List<RealMatrix> getStatisticsMeanHistory() {
        return statisticsMeanHistory;
    }

    /**
     * @return History of fitness values.
     */
    public List<Double> getStatisticsFitnessHistory() {
        return statisticsFitnessHistory;
    }

    /**
     * @return History of D matrix.
     */
    public List<RealMatrix> getStatisticsDHistory() {
        return statisticsDHistory;
    }

    /** {@inheritDoc} */
    @Override
    protected RealPointValuePair doOptimize() {
        checkParameters();
         // -------------------- Initialization --------------------------------
        isMinimize = getGoalType().equals(GoalType.MINIMIZE);
        final FitnessFunction fitfun = new FitnessFunction();
        final double[] guess = fitfun.encode(getStartPoint());
        // number of objective variables/problem dimension
        dimension = guess.length;
        initializeCMA(guess);
        iterations = 0;
        double bestValue = fitfun.value(guess);
        push(fitnessHistory, bestValue);
        RealPointValuePair optimum = new RealPointValuePair(getStartPoint(),
                isMinimize ? bestValue : -bestValue);
        RealPointValuePair lastResult = null;

        // -------------------- Generation Loop --------------------------------

        generationLoop:
            for (iterations = 1; iterations <= maxIterations; iterations++) {
                // Generate and evaluate lambda offspring
                RealMatrix arz = randn1(dimension, lambda);
                RealMatrix arx = zeros(dimension, lambda);
                double[] fitness = new double[lambda];
                // generate random offspring
                for (int k = 0; k < lambda; k++) {
                    RealMatrix arxk = null;
                    for (int i = 0; i < checkFeasableCount+1; i++) {
                        if (diagonalOnly <= 0) {
                            arxk = xmean.add(BD.multiply(arz.getColumnMatrix(k))
                                    .scalarMultiply(sigma)); // m + sig * Normal(0,C)
                        } else {
                            arxk = xmean.add(times(diagD,arz.getColumnMatrix(k))
                                    .scalarMultiply(sigma));
                        }
                        if (i >= checkFeasableCount || fitfun.isFeasible(arxk.getColumn(0))) {
                            break;
                        }
                        // regenerate random arguments for row
                        arz.setColumn(k, randn(dimension));
                    }
                    copyColumn(arxk, 0, arx, k);
                    try {
                        fitness[k] = fitfun.value(arx.getColumn(k)); // compute fitness
                    } catch (TooManyEvaluationsException e) {
                        break generationLoop;
                    }
                }
                // Sort by fitness and compute weighted mean into xmean
                int[] arindex = sortedIndices(fitness);
                // Calculate new xmean, this is selection and recombination
                RealMatrix xold = xmean; // for speed up of Eq. (2) and (3)
                RealMatrix bestArx = selectColumns(arx, MathArrays.copyOf(arindex, mu));
                xmean = bestArx.multiply(weights);
                RealMatrix bestArz = selectColumns(arz, MathArrays.copyOf(arindex, mu));
                RealMatrix zmean = bestArz.multiply(weights);
                boolean hsig = updateEvolutionPaths(zmean, xold);
                if (diagonalOnly <= 0) {
                    updateCovariance(hsig, bestArx, arz, arindex, xold);
                } else {
                    updateCovarianceDiagonalOnly(hsig, bestArz, xold);
                }
                // Adapt step size sigma - Eq. (5)
                sigma *= Math.exp(Math.min(1.0,(normps/chiN - 1.)*cs/damps));
                double bestFitness = fitness[arindex[0]];
                double worstFitness = fitness[arindex[arindex.length-1]];
                if (bestValue > bestFitness) {
                    bestValue = bestFitness;
                    lastResult = optimum;
                    optimum = new RealPointValuePair(
                            fitfun.decode(bestArx.getColumn(0)),
                            isMinimize ? bestFitness : -bestFitness);
                    if (getConvergenceChecker() != null && lastResult != null) {
                        if (getConvergenceChecker().converged(iterations, optimum, lastResult)) {
                            break generationLoop;
                        }
                    }
                }
                // handle termination criteria
                // Break, if fitness is good enough
                if (stopfitness != 0) { // only if stopfitness is defined
                    if (bestFitness < (isMinimize ? stopfitness : -stopfitness)) {
                        break generationLoop;
                    }
                }
                double[] sqrtDiagC = sqrt(diagC).getColumn(0);
                double[] pcCol = pc.getColumn(0);
                for (int i = 0; i < dimension; i++) {
                    if (sigma*(Math.max(Math.abs(pcCol[i]), sqrtDiagC[i])) > stopTolX) {
                        break;
                    }
                    if (i >= dimension-1) {
                        break generationLoop;
                    }
                }
                for (int i = 0; i < dimension; i++) {
                    if (sigma*sqrtDiagC[i] > stopTolUpX) {
                        break generationLoop;
                    }
                }
                double historyBest = min(fitnessHistory);
                double historyWorst = max(fitnessHistory);
                if (iterations > 2 && Math.max(historyWorst, worstFitness) -
                        Math.min(historyBest, bestFitness) < stopTolFun) {
                    break generationLoop;
                }
                if (iterations > fitnessHistory.length &&
                        historyWorst-historyBest < stopTolHistFun) {
                    break generationLoop;
                }
                // condition number of the covariance matrix exceeds 1e14
                if (max(diagD)/min(diagD) > 1e7) {
                    break generationLoop;
                }
                // user defined termination
                if (getConvergenceChecker() != null) {
                    RealPointValuePair current =
                        new RealPointValuePair(bestArx.getColumn(0),
                                isMinimize ? bestFitness : -bestFitness);
                    if (lastResult != null &&
                        getConvergenceChecker().converged(iterations, current, lastResult)) {
                        break generationLoop;
                    }
                    lastResult = current;
                }
                // Adjust step size in case of equal function values (flat fitness)
                if (bestValue == fitness[arindex[(int)(0.1+lambda/4.)]]) {
                    sigma = sigma * Math.exp(0.2+cs/damps);
                }
                if (iterations > 2 && Math.max(historyWorst, bestFitness) -
                        Math.min(historyBest, bestFitness) == 0) {
                    sigma = sigma * Math.exp(0.2+cs/damps);
                }
                // store best in history
                push(fitnessHistory,bestFitness);
                fitfun.setValueRange(worstFitness-bestFitness);
                if (generateStatistics) {
                    statisticsSigmaHistory.add(sigma);
                    statisticsFitnessHistory.add(bestFitness);
                    statisticsMeanHistory.add(xmean.transpose());
                    statisticsDHistory.add(diagD.transpose().scalarMultiply(1E5));
                }
            }
        return optimum;
    }

    /**
     * Checks dimensions and values of boundaries and inputSigma if defined.
     */
    private void checkParameters() {
        double[] init = getStartPoint();
        if (boundaries != null) {
            if (boundaries.length != 2) {
                throw new MultiDimensionMismatchException(
                        new Integer[] { boundaries.length },
                        new Integer[] { 2 });
            }
            if (boundaries[0] == null || boundaries[1] == null) {
                throw new NoDataException();
            }
            if (boundaries[0].length != init.length) {
                throw new MultiDimensionMismatchException(
                        new Integer[] { boundaries[0].length },
                        new Integer[] { init.length });
            }
            if (boundaries[1].length != init.length) {
                throw new MultiDimensionMismatchException(
                        new Integer[] { boundaries[1].length },
                        new Integer[] { init.length });
            }
            for (int i = 0; i < init.length; i++) {
                if (boundaries[0][i] > init[i] || boundaries[1][i] < init[i]) {
                    throw new OutOfRangeException(init[i], boundaries[0][i],
                            boundaries[1][i]);
                }
            }
        }
        if (inputSigma != null) {
            if (inputSigma.length != init.length) {
                throw new MultiDimensionMismatchException(
                        new Integer[] { inputSigma.length },
                        new Integer[] { init.length });
            }
            for (int i = 0; i < init.length; i++) {
                if (inputSigma[i] < 0) {
                    throw new NotPositiveException(inputSigma[i]);
                }
                if (boundaries != null) {
                    if (inputSigma[i] > 1.0) {
                        throw new OutOfRangeException(inputSigma[i], 0, 1.0);
                    }
                }
            }
        }
    }

    /**
     * Initialization of the dynamic search parameters
     *
     * @param guess Initial guess for the arguments of the fitness function.
     */
    private void initializeCMA(double[] guess) {
        if (lambda <= 0) {
            lambda = 4 + (int) (3. * Math.log(dimension));
        }
        // initialize sigma
        double[][] sigmaArray = new double[guess.length][1];
        for (int i = 0; i < guess.length; i++) {
            sigmaArray[i][0] = inputSigma != null ? inputSigma[i] : 0.3;
        }
        RealMatrix insigma = new Array2DRowRealMatrix(sigmaArray, false);
        sigma = max(insigma); // overall standard deviation

        // initialize termination criteria
        stopTolUpX = 1e3 * max(insigma);
        stopTolX = 1e-11 * max(insigma);
        stopTolFun = 1e-12;
        stopTolHistFun = 1e-13;

        // initialize selection strategy parameters
        mu = lambda / 2; // number of parents/points for recombination
        logMu2 = Math.log(mu + 0.5);
        weights = log(sequence(1, mu, 1)).scalarMultiply(-1.).scalarAdd(logMu2);
        double sumw = 0;
        double sumwq = 0;
        for (int i = 0; i < mu; i++) {
            double w = weights.getEntry(i, 0);
            sumw += w;
            sumwq += w * w;
        }
        weights = weights.scalarMultiply(1. / sumw);
        mueff = sumw * sumw / sumwq; // variance-effectiveness of sum w_i x_i

        // initialize dynamic strategy parameters and constants
        cc = (4. + mueff / dimension) /
                (dimension + 4. + 2. * mueff / dimension);
        cs = (mueff + 2.) / (dimension + mueff + 3.);
        damps = (1. + 2. * Math.max(0, Math.sqrt((mueff - 1.) /
                (dimension + 1.)) - 1.)) *
                Math.max(0.3, 1. - dimension /
                        (1e-6 + Math.min(maxIterations, getMaxEvaluations() /
                                lambda))) + cs; // minor increment
        ccov1 = 2. / ((dimension + 1.3) * (dimension + 1.3) + mueff);
        ccovmu = Math.min(1 - ccov1, 2. * (mueff - 2. + 1. / mueff) /
                ((dimension + 2.) * (dimension + 2.) + mueff));
        ccov1Sep = Math.min(1, ccov1 * (dimension + 1.5) / 3.);
        ccovmuSep = Math.min(1 - ccov1, ccovmu * (dimension + 1.5) / 3.);
        chiN = Math.sqrt(dimension) *
                (1. - 1. / (4. * dimension) + 1 / (21. * dimension * dimension));
        // intialize CMA internal values - updated each generation
        xmean = MatrixUtils.createColumnRealMatrix(guess); // objective
                                                           // variables
        diagD = insigma.scalarMultiply(1. / sigma);
        diagC = square(diagD);
        pc = zeros(dimension, 1); // evolution paths for C and sigma
        ps = zeros(dimension, 1); // B defines the coordinate system
        normps = ps.getFrobeniusNorm();

        B = eye(dimension, dimension);
        D = ones(dimension, 1); // diagonal D defines the scaling
        BD = times(B, repmat(diagD.transpose(), dimension, 1));
        C = B.multiply(diag(square(D)).multiply(B.transpose())); // covariance
        historySize = 10 + (int) (3. * 10. * dimension / lambda);
        fitnessHistory = new double[historySize]; // history of fitness values
        for (int i = 0; i < historySize; i++) {
            fitnessHistory[i] = Double.MAX_VALUE;
        }
    }

    /**
     * Update of the evolution paths ps and pc.
     *
     * @param zmean Weighted row matrix of the gaussian random numbers generating
     * the current offspring.
     * @param xold xmean matrix of the previous generation.
     * @return hsig flag indicating a small correction.
     */
    private boolean updateEvolutionPaths(RealMatrix zmean, RealMatrix xold) {
        ps = ps.scalarMultiply(1. - cs).add(
                B.multiply(zmean).scalarMultiply(
                        Math.sqrt(cs * (2. - cs) * mueff)));
        normps = ps.getFrobeniusNorm();
        boolean hsig = normps /
            Math.sqrt(1. - Math.pow(1. - cs, 2. * iterations)) /
                chiN < 1.4 + 2. / (dimension + 1.);
        pc = pc.scalarMultiply(1. - cc);
        if (hsig) {
            pc = pc.add(xmean.subtract(xold).scalarMultiply(
                    Math.sqrt(cc * (2. - cc) * mueff) / sigma));
        }
        return hsig;
    }

    /**
     * Update of the covariance matrix C for diagonalOnly > 0
     *
     * @param hsig Flag indicating a small correction.
     * @param bestArz Fitness-sorted matrix of the gaussian random values of the
     * current offspring.
     * @param xold xmean matrix of the previous generation.
     */
    private void updateCovarianceDiagonalOnly(boolean hsig,
                                              final RealMatrix bestArz,
                                              final RealMatrix xold) {
        // minor correction if hsig==false
        double oldFac = hsig ? 0 : ccov1Sep * cc * (2. - cc);
        oldFac += 1. - ccov1Sep - ccovmuSep;
        diagC = diagC.scalarMultiply(oldFac) // regard old matrix
                // plus rank one update
                .add(square(pc).scalarMultiply(ccov1Sep))
                // plus rank mu update
                .add((times(diagC, square(bestArz).multiply(weights)))
                        .scalarMultiply(ccovmuSep));
        diagD = sqrt(diagC); // replaces eig(C)
        if (diagonalOnly > 1 && iterations > diagonalOnly) {
            // full covariance matrix from now on
            diagonalOnly = 0;
            B = eye(dimension, dimension);
            BD = diag(diagD);
            C = diag(diagC);
        }
    }

    /**
     * Update of the covariance matrix C.
     *
     * @param hsig Flag indicating a small correction.
     * @param bestArx Fitness-sorted matrix of the argument vectors producing the
     * current offspring.
     * @param arz Unsorted matrix containing the gaussian random values of the
     * current offspring.
     * @param arindex Indices indicating the fitness-order of the current offspring.
     * @param xold xmean matrix of the previous generation.
     */
    private void updateCovariance(boolean hsig, final RealMatrix bestArx,
            final RealMatrix arz, final int[] arindex, final RealMatrix xold) {
        double negccov = 0;
        if (ccov1 + ccovmu > 0) {
            RealMatrix arpos = bestArx.subtract(repmat(xold, 1, mu))
                    .scalarMultiply(1. / sigma); // mu difference vectors
            RealMatrix roneu = pc.multiply(pc.transpose())
                    .scalarMultiply(ccov1); // rank one update
            // minor correction if hsig==false
            double oldFac = hsig ? 0 : ccov1 * cc * (2. - cc);
            oldFac += 1. - ccov1 - ccovmu;
            if (isActiveCMA) {
                // Adapt covariance matrix C active CMA
                negccov = (1. - ccovmu) * 0.25 * mueff /
                (Math.pow(dimension + 2., 1.5) + 2. * mueff);
                double negminresidualvariance = 0.66;
                // keep at least 0.66 in all directions, small popsize are most
                // critical
                double negalphaold = 0.5; // where to make up for the variance
                                          // loss,
                // prepare vectors, compute negative updating matrix Cneg
                int[] arReverseIndex = reverse(arindex);
                RealMatrix arzneg
                    = selectColumns(arz, MathArrays.copyOf(arReverseIndex, mu));
                RealMatrix arnorms = sqrt(sumRows(square(arzneg)));
                int[] idxnorms = sortedIndices(arnorms.getRow(0));
                RealMatrix arnormsSorted = selectColumns(arnorms, idxnorms);
                int[] idxReverse = reverse(idxnorms);
                RealMatrix arnormsReverse = selectColumns(arnorms, idxReverse);
                arnorms = divide(arnormsReverse, arnormsSorted);
                int[] idxInv = inverse(idxnorms);
                RealMatrix arnormsInv = selectColumns(arnorms, idxInv);
                // check and set learning rate negccov
                double negcovMax = (1. - negminresidualvariance) /
                        square(arnormsInv).multiply(weights).getEntry(0, 0);
                if (negccov > negcovMax) {
                    negccov = negcovMax;
                }
                arzneg = times(arzneg, repmat(arnormsInv, dimension, 1));
                RealMatrix artmp = BD.multiply(arzneg);
                RealMatrix Cneg = artmp.multiply(diag(weights)).multiply(
                        artmp.transpose());
                oldFac += negalphaold * negccov;
                C = C.scalarMultiply(oldFac)
                        // regard old matrix
                        .add(roneu)
                        // plus rank one update
                        .add(arpos.scalarMultiply(
                                // plus rank mu update
                                ccovmu + (1. - negalphaold) * negccov)
                                .multiply(
                                        times(repmat(weights, 1, dimension),
                                                arpos.transpose())))
                        .subtract(Cneg.scalarMultiply(negccov));
            } else {
                // Adapt covariance matrix C - nonactive
                C = C.scalarMultiply(oldFac) // regard old matrix
                        .add(roneu)
                        // plus rank one update
                        .add(arpos.scalarMultiply(ccovmu) // plus rank mu update
                                .multiply(
                                        times(repmat(weights, 1, dimension),
                                                arpos.transpose())));
            }
        }
        updateBD(negccov);
    }

    /**
     * Update B and D from C.
     *
     * @param negccov Negative covariance factor.
     */
    private void updateBD(double negccov) {
        if (ccov1 + ccovmu + negccov > 0 &&
                (iterations % 1. / (ccov1 + ccovmu + negccov) / dimension / 10.) < 1.) {
            // to achieve O(N^2)
            C = triu(C, 0).add(triu(C, 1).transpose());
            // enforce symmetry to prevent complex numbers
            EigenDecomposition eig = new EigenDecomposition(C, 1.0);
            B = eig.getV(); // eigen decomposition, B==normalized eigenvectors
            D = eig.getD();
            diagD = diag(D);
            if (min(diagD) <= 0) {
                for (int i = 0; i < dimension; i++) {
                    if (diagD.getEntry(i, 0) < 0) {
                        diagD.setEntry(i, 0, 0.);
                    }
                }
                double tfac = max(diagD) / 1e14;
                C = C.add(eye(dimension, dimension).scalarMultiply(tfac));
                diagD = diagD.add(ones(dimension, 1).scalarMultiply(tfac));
            }
            if (max(diagD) > 1e14 * min(diagD)) {
                double tfac = max(diagD) / 1e14 - min(diagD);
                C = C.add(eye(dimension, dimension).scalarMultiply(tfac));
                diagD = diagD.add(ones(dimension, 1).scalarMultiply(tfac));
            }
            diagC = diag(C);
            diagD = sqrt(diagD); // D contains standard deviations now
            BD = times(B, repmat(diagD.transpose(), dimension, 1)); // O(n^2)
        }
    }

    /**
     * Pushes the current best fitness value in a history queue.
     *
     * @param vals History queue.
     * @param val Current best fitness value.
     */
    private static void push(double[] vals, double val) {
        for (int i = vals.length-1; i > 0; i--) {
            vals[i] = vals[i-1];
        }
        vals[0] = val;
    }

    /**
     * Sorts fitness values.
     *
     * @param doubles Array of values to be sorted.
     * @return a sorted array of indices pointing into doubles.
     */
    private int[] sortedIndices(final double[] doubles) {
        DoubleIndex[] dis = new DoubleIndex[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            dis[i] = new DoubleIndex(doubles[i], i);
        }
        Arrays.sort(dis);
        int[] indices = new int[doubles.length];
        for (int i = 0; i < doubles.length; i++) {
            indices[i] = dis[i].index;
        }
        return indices;
    }

    /**
     * Used to sort fitness values. Sorting is always in lower value first
     * order.
     */
    private static class DoubleIndex implements Comparable<DoubleIndex> {
        /** Value to compare. */
        private double value;
        /** Index into sorted array. */
        private int index;

        /**
         * @param value Value to compare.
         * @param index Index into sorted array.
         */
        DoubleIndex(double value, int index) {
            this.value = value;
            this.index = index;
        }

        /** {@inheritDoc} */
        public int compareTo(DoubleIndex o) {
            return Double.compare(value, o.value);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object other) {

            if (this == other) {
                return true;
            }

            if (other instanceof DoubleIndex) {
                return Double.compare(value, ((DoubleIndex) other).value) == 0;
            }

            return false;

        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            long bits = Double.doubleToLongBits(value);
            return (int) ((1438542 ^ (bits >>> 32) ^ bits) & 0xffffffff);
        }

    }

    /**
     * Normalizes fitness values to the range [0,1]. Adds a penalty to the
     * fitness value if out of range. The penalty is adjusted by calling
     * setValueRange().
     */
    private class FitnessFunction {
        /** Determines the penalty for boundary violations */
        private double valueRange;
        /**
         * Flag indicating whether the objective variables are forced into their
         * bounds if defined
         */
        private boolean isRepairMode;

        /** Simple constructor.
         */
        public FitnessFunction() {
            valueRange = 1.0;
            isRepairMode = true;
        }

        /**
         * @param x Original objective variables.
         * @return the normalized objective variables.
         */
        public double[] encode(final double[] x) {
            if (boundaries == null) {
                return x;
            }
            double[] res = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                double diff = boundaries[1][i] - boundaries[0][i];
                res[i] = (x[i] - boundaries[0][i]) / diff;
            }
            return res;
        }

        /**
         * @param x Normalized objective variables.
         * @return the original objective variables.
         */
        public double[] decode(final double[] x) {
            if (boundaries == null) {
                return x;
            }
            double[] res = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                double diff = boundaries[1][i] - boundaries[0][i];
                res[i] = diff * x[i] + boundaries[0][i];
            }
            return res;
        }

        /**
         * @param point Normalized objective variables.
         * @return the objective value + penalty for violated bounds.
         */
        public double value(final double[] point) {
            double value;
            if (boundaries != null && isRepairMode) {
                double[] repaired = repair(point);
                value = CMAESOptimizer.this
                        .computeObjectiveValue(decode(repaired)) +
                        penalty(point, repaired);
            } else {
                value = CMAESOptimizer.this
                        .computeObjectiveValue(decode(point));
            }
            return isMinimize ? value : -value;
        }

        /**
         * @param x Normalized objective variables.
         * @return {@code true} if in bounds.
         */
        public boolean isFeasible(final double[] x) {
            if (boundaries == null) {
                return true;
            }
            for (int i = 0; i < x.length; i++) {
                if (x[i] < 0) {
                    return false;
                }
                if (x[i] > 1.0) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @param valueRange Adjusts the penalty computation.
         */
        public void setValueRange(double valueRange) {
            this.valueRange = valueRange;
        }

        /**
         * @param x Normalized objective variables.
         * @return the repaired objective variables - all in bounds.
         */
        private double[] repair(final double[] x) {
            double[] repaired = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                if (x[i] < 0) {
                    repaired[i] = 0;
                } else if (x[i] > 1.0) {
                    repaired[i] = 1.0;
                } else {
                    repaired[i] = x[i];
                }
            }
            return repaired;
        }

        /**
         * @param x Normalized objective variables.
         * @param repaired Repaired objective variables.
         * @return Penalty value according to the violation of the bounds.
         */
        private double penalty(final double[] x, final double[] repaired) {
            double penalty = 0;
            for (int i = 0; i < x.length; i++) {
                double diff = Math.abs(x[i] - repaired[i]);
                penalty += diff * valueRange;
            }
            return isMinimize ? penalty : -penalty;
        }
    }

    // -----Matrix utility functions similar to the Matlab build in functions------

    /**
     * @param m Input matrix
     * @return Matrix representing the element-wise logarithm of m.
     */
    private static RealMatrix log(final RealMatrix m) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = Math.log(m.getEntry(r, c));
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m
     *            Input matrix
     * @return Matrix representing the element-wise square root of m.
     */
    private static RealMatrix sqrt(final RealMatrix m) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = Math.sqrt(m.getEntry(r, c));
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix
     * @return Matrix representing the element-wise square (^2) of m.
     */
    private static RealMatrix square(final RealMatrix m) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                d[r][c] = e * e;
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix 1.
     * @param n Input matrix 2.
     * @return the matrix where the elements of m and n are element-wise multiplied.
     */
    private static RealMatrix times(final RealMatrix m, final RealMatrix n) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = m.getEntry(r, c) * n.getEntry(r, c);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix 1.
     * @param n Input matrix 2.
     * @return Matrix where the elements of m and n are element-wise divided.
     */
    private static RealMatrix divide(final RealMatrix m, final RealMatrix n) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = m.getEntry(r, c) / n.getEntry(r, c);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix.
     * @param cols Columns to select.
     * @return Matrix representing the selected columns.
     */
    private static RealMatrix selectColumns(final RealMatrix m, final int[] cols) {
        double[][] d = new double[m.getRowDimension()][cols.length];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < cols.length; c++) {
                d[r][c] = m.getEntry(r, cols[c]);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix.
     * @param k Diagonal position.
     * @return Upper triangular part of matrix.
     */
    private static RealMatrix triu(final RealMatrix m, int k) {
        double[][] d = new double[m.getRowDimension()][m.getColumnDimension()];
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                d[r][c] = r <= c - k ? m.getEntry(r, c) : 0;
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix.
     * @return Row matrix representing the sums of the rows.
     */
    private static RealMatrix sumRows(final RealMatrix m) {
        double[][] d = new double[1][m.getColumnDimension()];
        for (int c = 0; c < m.getColumnDimension(); c++) {
            double sum = 0;
            for (int r = 0; r < m.getRowDimension(); r++) {
                sum += m.getEntry(r, c);
            }
            d[0][c] = sum;
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix.
     * @return the diagonal n-by-n matrix if m is a column matrix or the column
     * matrix representing the diagonal if m is a n-by-n matrix.
     */
    private static RealMatrix diag(final RealMatrix m) {
        if (m.getColumnDimension() == 1) {
            double[][] d = new double[m.getRowDimension()][m.getRowDimension()];
            for (int i = 0; i < m.getRowDimension(); i++) {
                d[i][i] = m.getEntry(i, 0);
            }
            return new Array2DRowRealMatrix(d, false);
        } else {
            double[][] d = new double[m.getRowDimension()][1];
            for (int i = 0; i < m.getColumnDimension(); i++) {
                d[i][0] = m.getEntry(i, i);
            }
            return new Array2DRowRealMatrix(d, false);
        }
    }

    /**
     * Copies a column from m1 to m2.
     *
     * @param m1 Source matrix 1.
     * @param col1 Source column.
     * @param m2 Target matrix.
     * @param col2 Target column.
     */
    private static void copyColumn(final RealMatrix m1, int col1, RealMatrix m2, int col2) {
        for (int i = 0; i < m1.getRowDimension(); i++) {
            m2.setEntry(i, col2, m1.getEntry(i, col1));
        }
    }

    /**
     * @param n Number of rows.
     * @param m Number of columns.
     * @return n-by-m matrix filled with 1.
     */
    private static RealMatrix ones(int n, int m) {
        double[][] d = new double[n][m];
        for (int r = 0; r < n; r++) {
            Arrays.fill(d[r], 1.0);
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param n Number of rows.
     * @param m Number of columns.
     * @return n-by-m matrix of 0.0-values, diagonal has values 1.0.
     */
    private static RealMatrix eye(int n, int m) {
        double[][] d = new double[n][m];
        for (int r = 0; r < n; r++) {
            if (r < m) {
                d[r][r] = 1;
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param n Number of rows.
     * @param m Number of columns.
     * @return n-by-m matrix of 0.0-values.
     */
    private static RealMatrix zeros(int n, int m) {
        return new Array2DRowRealMatrix(n, m);
    }

    /**
     * @param mat Input matrix.
     * @param n Number of row replicates.
     * @param m Number of column replicates.
     * @return a matrix which replicates the input matrix in both directions.
     */
    private static RealMatrix repmat(final RealMatrix mat, int n, int m) {
        int rd = mat.getRowDimension();
        int cd = mat.getColumnDimension();
        double[][] d = new double[n * rd][m * cd];
        for (int r = 0; r < n * rd; r++) {
            for (int c = 0; c < m * cd; c++) {
                d[r][c] = mat.getEntry(r % rd, c % cd);
            }
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param start Start value.
     * @param end End value.
     * @param step Step size.
     * @return a sequence as column matrix.
     */
    private static RealMatrix sequence(double start, double end, double step) {
        int size = (int) ((end - start) / step + 1);
        double[][] d = new double[size][1];
        double value = start;
        for (int r = 0; r < size; r++) {
            d[r][0] = value;
            value += step;
        }
        return new Array2DRowRealMatrix(d, false);
    }

    /**
     * @param m Input matrix.
     * @return the maximum of the matrix element values.
     */
    private static double max(final RealMatrix m) {
        double max = -Double.MAX_VALUE;
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                if (max < e) {
                    max = e;
                }
            }
        }
        return max;
    }

    /**
     * @param m Input matrix.
     * @return the minimum of the matrix element values.
     */
    private static double min(final RealMatrix m) {
        double min = Double.MAX_VALUE;
        for (int r = 0; r < m.getRowDimension(); r++) {
            for (int c = 0; c < m.getColumnDimension(); c++) {
                double e = m.getEntry(r, c);
                if (min > e) {
                    min = e;
                }
            }
        }
        return min;
    }

    /**
     * @param m Input array.
     * @return the maximum of the array values.
     */
    private static double max(final double[] m) {
        double max = -Double.MAX_VALUE;
        for (int r = 0; r < m.length; r++) {
            if (max < m[r]) {
                max = m[r];
            }
        }
        return max;
    }

    /**
     * @param m Input array.
     * @return the minimum of the array values.
     */
    private static double min(final double[] m) {
        double min = Double.MAX_VALUE;
        for (int r = 0; r < m.length; r++) {
            if (min > m[r]) {
                min = m[r];
            }
        }
        return min;
    }

    /**
     * @param indices Input index array.
     * @return the inverse of the mapping defined by indices.
     */
    private static int[] inverse(final int[] indices) {
        int[] inverse = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            inverse[indices[i]] = i;
        }
        return inverse;
    }

    /**
     * @param indices Input index array.
     * @return the indices in inverse order (last is first).
     */
    private static int[] reverse(final int[] indices) {
        int[] reverse = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            reverse[i] = indices[indices.length - i - 1];
        }
        return reverse;
    }

    /**
     * @param size Length of random array.
     * @return an array of Gaussian random numbers.
     */
    private double[] randn(int size) {
        double[] randn = new double[size];
        for (int i = 0; i < size; i++) {
            randn[i] = random.nextGaussian();
        }
        return randn;
    }

    /**
     * @param size Number of rows.
     * @param popSize Population size.
     * @return a 2-dimensional matrix of Gaussian random numbers.
     */
    private RealMatrix randn1(int size, int popSize) {
        double[][] d = new double[size][popSize];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < popSize; c++) {
                d[r][c] = random.nextGaussian();
            }
        }
        return new Array2DRowRealMatrix(d, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3666.java