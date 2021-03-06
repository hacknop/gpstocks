
package gpfinance.algorithm;

import gpfinance.datatypes.FitnessData;
import gpfinance.U;
import gpfinance.datatypes.Decision;
import gpfinance.datatypes.Fitness;
import gpfinance.datatypes.Indicator;
import gpfinance.datatypes.Security;
import gpfinance.tree.DecisionTree;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Individual {

    // Chromosome & 'cached' fitness calculation
    private DecisionTree tree;
    private Fitness fitness = new Fitness();
    
    // Static data
    public static boolean CONSIDER_SIZE = true;
    public static double SIZE_CONTRIBUTION;
    public static FitnessData fitnessData;

    public Individual() {
    }

    public Individual(char type) {
        this.tree = new DecisionTree(type);
    }

    public Individual(char type, int treesize) {
        this.tree = new DecisionTree(type, treesize);
    }

    public Individual(DecisionTree tree) {
        this.tree = tree;
    }

    public Individual(DecisionTree tree, Fitness fitness) {
        this.tree = tree;
        this.fitness = fitness;
    }

    @Override
    public Individual clone() {
        return new Individual(this.tree.clone(), this.fitness.clone());
    }

    public void measure(int generation, ArrayList<Security> securities) {
        Decision[] decisions = tree.evaluate(securities);
        //System.out.println(decisions);
        
        // Set fitness measures
        fitness.returnValue = fitnessData.calculateReturn(decisions);
    }

    public double getFitness() {
        return this.fitness.getFitness();
    }

    public double getHeterogeneity() {
        return tree.heterogeneity();
    }
    
    public Indicator getMostOccuringIndicator(){
        return tree.getMostOccuringIndicator();
    }

    public DecisionTree getTree() {
        return tree;
    }

    public static Comparator<Individual> AscendingFitness = new Comparator<Individual>() {
        @Override
        public int compare(Individual o1, Individual o2) {
            Double d1 = o1.fitness.getFitness() + (CONSIDER_SIZE ? (Individual.SIZE_CONTRIBUTION / o1.getTree().size()) : 0);
            Double d2 = o2.fitness.getFitness() + (CONSIDER_SIZE ? (Individual.SIZE_CONTRIBUTION / o2.getTree().size()) : 0);
            return d1.compareTo(d2);
        }
    };

    public static Comparator<Individual> DescendingFitness = new Comparator<Individual>() {
        @Override
        public int compare(Individual o1, Individual o2) {
            Double d1 = o1.fitness.getFitness() + (CONSIDER_SIZE ? (Individual.SIZE_CONTRIBUTION / o1.getTree().size()) : 0);
            Double d2 = o2.fitness.getFitness() + (CONSIDER_SIZE ? (Individual.SIZE_CONTRIBUTION / o2.getTree().size()) : 0);
            return d2.compareTo(d1);
        }
    };

    public void print() {
        U.m("f() = " + fitness.getFitness());
        tree.print();
    }
    
    public int size() {
        return tree.size();
    }
    /*
     * Mutation methods, delegate to tree. 
     */
    public void mutateGrow() {
        tree.insertRandom();
    }

    public void mutateTrunc() {
        tree.removeRandomLimitedDepth();
    }

    public void mutateGauss() {
        tree.gaussRandom();
    }

    public void mutateSwapInequality() {
        tree.swapRandomInequality();
    }

    public void mutateLeaf() {
        tree.mutateTerminalNode();
    }

    public void mutateNonLeaf() {
        tree.mutateNonterminalNode();
    }
}
