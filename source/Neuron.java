import java.util.ArrayList;
import java.util.Random;

public class Neuron
{
    //features
    private int neuronId;
    private int numberOfWeightsFromNextNeuron;
    private double outcome;
    private double gradient;
    private double eta;
    private double momentum;
    private ArrayList <Synapse> weights = new ArrayList <Synapse> ( );

    
    public Neuron ( double eta, double alpha, int neuronId, int numberOfWeightsFromNextNeuron )
    {
        this.neuronId = neuronId;
        this.numberOfWeightsFromNextNeuron = numberOfWeightsFromNextNeuron;
        this.eta = eta;
        this.momentum = alpha;
        this.gradient = 0;
        
        for ( int sI = 0; sI < numberOfWeightsFromNextNeuron; sI ++ )
        {
            weights.add ( new Synapse ( ) );
            weights.get ( sI ).setWeight ( new Random ( ).nextDouble ( ) );
        }
    }
    
    public double getOutcome ( )
    {
        return outcome;
    }
    public double getGradient ( )
    {
        return gradient;
    }  
    public ArrayList <Synapse> getWeights ( )
    {
        return weights;
    }
    public double getActivation ( double value )
    {
        return Math.tanh ( value );
    }
    public double getPrimeActivation ( double value )
    {
        return 1 - Math.pow ( Math.tanh ( value ) , 2 );
    }
    public double getDistributedWeightSigma ( Layer nextLayer )
    {
        double sigma =  0;
        
        for ( int nLI = 0; nLI < nextLayer.size ( ) - 1; nLI ++ )
            sigma += getWeights ( ).get ( nLI ).getWeight ( ) * nextLayer.get ( nLI ).getGradient ( );
        
        return sigma;
    }
    
    public void setOutcome ( double value )
    {
        outcome = value;
    }
    
    public void setGradient ( double value )
    {
        gradient = value;
    }
    
    public void calculateOutcomeGradient ( int target )
    {
        double delta = target - outcome;
        
        setGradient ( getPrimeActivation ( outcome ) * delta );
    }
    
    public void calculateHiddenGradient ( Layer nextLayer )
    {
        double delta = getDistributedWeightSigma ( nextLayer );
        
        setGradient ( getPrimeActivation ( outcome ) * delta );
    }    
    
    public void doForwardPropagation ( Layer priorLayer ) 
    {
        double sigma = 0;
        
        for ( int pLI = 0; pLI < priorLayer.size ( ); pLI ++ )
        {
            sigma += priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getWeight ( ) * priorLayer.get ( pLI ).getOutcome ( );
        }
        
        setOutcome ( getActivation ( sigma ) );
    }
    
    public void updateWeights ( Layer priorLayer ) 
    {
        for ( int pLI = 0; pLI < priorLayer.size ( ); pLI ++ )
        {
            double priorDeltaWeight = priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getDeltaWeight ( );
            
            //newDeltaWeight = eta * thisGrad * thoseOut + alpha * pDW
            double newDeltaWeight = ( eta * gradient * priorLayer.get ( pLI ).getOutcome ( ) ) + ( momentum * priorDeltaWeight );
            
            priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).setDeltaWeight ( newDeltaWeight );
            priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).setWeight ( priorLayer.get ( pLI ).getWeights ( ).get ( neuronId ).getWeight ( ) + newDeltaWeight );
        }
    }   
}