package geneticAlgorithm;

public class Gene<T> implements Comparable<Gene<T>>{
    private double fitness;
    private T t;

    public double getFitness() {
        return fitness;
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    public T get() {
        return t;
    }
    public void set(T t){
        this.t = t;
    }

    @Override
    public int compareTo(Gene<T> o) {
        if(fitness < o.getFitness()){
            return 1;
        }else if(fitness == o.getFitness()){
            return 0;
        }else{
            return -1;
        }
    }
}
