package geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GAThreadExecutor<T> {

    int cores = Runtime.getRuntime().availableProcessors();
    ExecutorService e = Executors.newFixedThreadPool(cores);

    Evolvable<T> GA;

    public GAThreadExecutor(Evolvable<T> GA){
        this.GA = GA;
    }

    List<Future<?>> futures = new ArrayList<>();

    public void setFitness(ArrayList<Gene<T>> population) {
        futures.clear();

        for(Gene<T> gene : population) {
            Future<?> f = e.submit(() -> gene.setFitness(GA.fitness(gene.get())));
            futures.add(f);
        }

        //wait for all threads to be done
        for(Future<?> f : futures) {
            try {
                f.get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void shutdown(){
        e.shutdown();

        System.out.println("Executor shutdown");
    }
}
