package geneticAlgorithm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

public class GeneticAlgorithm<T> {

    ArrayList<Gene<T>> population = new ArrayList<>();  //Population is array of Gene obj's, which hold a single gene T

    Evolvable<T> GA;

    GAThreadExecutor<T> executor;

    Random rand = new Random();
    int epoch = -1;
    int pop;
    double eliteFrac = 5;

    public boolean printValues = false;
    public boolean writeValues = false;

    String ID = UUID.randomUUID().toString();
    public PrintWriter writer;

        public GeneticAlgorithm(Evolvable<T> GA, int pop){ //TODO: Allow user to specify threads / ppt
            this.GA = GA;
            this.pop = pop;
            executor = new GAThreadExecutor(GA);
        }

    public void iterate(int iterations){//run GA for given epochs
        initpopulation();

        System.out.println("Finished population init");

        if(writeValues) initWriter();

        System.out.println("Starting genetic algorithm");

        for(int i = 0; i < iterations; i++){
            fitness();
            output();
            crossover();
            mutation();
        }

        fitness(); //perform final fitness evaluation

        executor.shutdown();
    }

    /* Initialization Methods */

    public void initpopulation(){
        population.clear();

        for(int i = 0; i < pop; i++){
            population.add(new Gene<>());
        }

        for(Gene<T> gene : population){
            gene.set(GA.init()); //population filled with random genes
        }
    }

    public void initWriter(){
        System.out.println("UUID: " + ID);
        try {
            writer = new PrintWriter(GA.getType() + ID + ".txt", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void fitness(){
        epoch++;

        executor.setFitness(population);

        Collections.sort(population); //sort genes
    }

    public void output(){
        String values = epoch + "\t" + getMax() + "\t" + getMean() + "\t" + getMin();

        if (printValues) System.out.println(values);

        if (writeValues) {
            writer.println(values);
            writer.flush();

            if (epoch % 10 == 0) {
                GA.saveGene(epoch, population.get(0).get());
            }
        }
    }

    public void crossover(){//percent based selection + elitist based selection
        ArrayList<Gene<T>> buffer = new ArrayList<>();//store new values in buffer

        double overallfitness = 0;
        for(int i = 0; i < population.size(); i++){ //determine pooled fitness
            overallfitness += population.get(i).getFitness();
        }

        for(int i = 0; i < population.size(); i++){ //calculate percent fitness
            population.get(i).setFitness(population.get(i).getFitness()/overallfitness);
        }

        double curposition = 0;
        for(int i = 0; i < population.size(); i++){
            curposition += population.get(i).getFitness();
            population.get(i).setFitness(curposition);
        }

        for(int i = 0; i < population.size(); i++){
            if(i <= population.size()/eliteFrac){//elitist based selection, top //TODO: make this variable
                buffer.add(population.get(i));

            }else{
                Gene<T> newparent = new Gene<>(); //rest are % based

                newparent.set(GA.crossover(getRandParent().get(),getRandParent().get()));

                buffer.add(newparent);
            }
        }

        population = buffer;
    }

    public Gene<T> getRandParent(){
        double parent = rand.nextDouble();
        for(Gene<T> gene : population){
            if(parent <= gene.getFitness())
                return gene;
        }

        System.out.println("No parent found. Check if fitness is not equal to zero");
        return null;
    }

    public void mutation(){
        //new method does not mutate highest fitness solutions

        for(int i = (int) (population.size()/eliteFrac) + 1; i < population.size(); i++){
            GA.mutate(population.get(i));
        }
    }


    public ArrayList<Gene<T>> getPopulation(){
        return population;
    }

    //Return highest scoring fitness individual
    public T getBest(){
        return population.get(0).get();
    }

    /* Return the highest, lowest, and mean fitness values of population */

    public double getMax(){
        return population.get(0).getFitness();
    }

    public double getMean(){
        double fit = 0;
        for(Gene g : population){
            fit += g.getFitness();
        }

        return fit/population.size();
    }

    public double getMin(){
        return population.get(population.size() - 1).getFitness();
    }
}
