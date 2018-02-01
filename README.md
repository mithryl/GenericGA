# Generic Genetic Algorithm
Makes use of generics to allow for easily defined genetic algorithms

## Usage
Implement the Evolvable interface, then pass it as a parameter to GeneticAlgorithm, along with the population size

## Interface Overview
```Java
    T crossover(T parent1, T parent2);  //Return a new object that is a crossover of two parents

    T init();                           //Return a random individual of type T, used in the initial population initialization

    double fitness(T t);                //Fitness function for type T, must not be zero due to fitness selection method

    void mutate(Gene<T> gene);          //Perform mutations on type T stored in a Gene<T> object

    String getType();                   //Used in name of output file in the form: getType() + UUID

    void saveGene(int epoch, T t);      //Optional. User can choose to save high fitness individual passed to this method
```


Example:
```Java
  SomeImplementation ga = new SomeImplementation(); //create new object implementing Evolvable using, for example, byte[]
  
  GeneticAlgorithm<byte[]> geneticalgorithm = new GeneticAlgorithm<>(ga,50);  //create GA object with same type as interface
  geneticalgorithm.iterate(100);
```

