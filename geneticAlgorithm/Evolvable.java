package geneticAlgorithm;

/* Classes implementing Evolvable can be used in GeneticAlgorithm */

public interface Evolvable<T>{
    T crossover(T parent1, T parent2);  //Return a new object that is a crossover of two parents

    T init();                           //Return a random individual of type T, used in the initial population initialization

    double fitness(T t);                //Fitness function for type T, must not be zero due to fitness selection method

    void mutate(Gene<T> gene);          //Perform mutations on type T stored in a Gene<T> object

    String getType();                   //Used in name of output file in the form: getType() + UUID

    void saveGene(int epoch, T t);      //Optional. User can choose to save high fitness individual passed to this method
}
