public class NeuralNetwork
{
   private double eta = 0.2;
   private double alpha = 0.5;
   private Architecture architecture = new Architecture ( "2,2,1" );
   private Layers layers = new Layers ( );
 
   public NeuralNetwork ( )
   {
       
       for ( int lSI = 0; lSI < architecture.size ( ); lSI ++ )
       {
           Layer layer = new Layer ( );
           
           for ( int lI = 0; lI <= architecture.get ( lSI ); lI ++ )
           {
                int numberOfWeightsFromNextNeuron = lSI + 1 < architecture.size ( ) ? architecture.get ( lSI ) : 0;
                
                Neuron neuron = new Neuron ( eta, alpha, lI, numberOfWeightsFromNextNeuron );
                
                layer.add ( neuron );
                
                layer.get ( layer.size ( ) - 1 ).setOutcome ( 1.0 ); //bias neuron
           }
           
           layers.add ( layer ); 
       }
    } 
       
   //do forward propagation
   void doForwardPropagation ( int inputs [ ] )
   {
       //setup layer 0
       for ( int iI = 0; iI < inputs.length; iI ++ )
           layers.get ( 0 ).get  ( iI ).setOutcome ( inputs [ iI ] );
           
           
       //setup layers 1+
       for ( int lSI = 1; lSI < architecture.size ( ); lSI ++)
       {
           Layer currentLayer = layers.get ( lSI );
           Layer priorLayer = layers.get ( lSI - 1 );
           
           for ( int lI = 0; lI < architecture.get ( lSI ); lI ++ )
           {
               currentLayer.get ( lI ).doForwardPropagation ( priorLayer );
           }
       }
   }
   
   
   //do backward propagation
   public void doBackwardPropagation ( int target )
   {
        //calc outcome grads
        layers.get ( layers.size ( ) - 1 ).get ( 0 ).calculateOutcomeGradient ( target );
        
        //calc hidden gradients
        for ( int lSI = layers.size ( ) - 2; lSI > 0; lSI -- )
        {
            Layer currentLayer = layers.get ( lSI );
            Layer nextLayer = layers.get ( lSI + 1 );
            
            for ( int lI = 0; lI < layers.get ( lSI ).size ( ); lI ++ )
            {
                currentLayer.get ( lI ).calculateHiddenGradient ( nextLayer );
            }
        }
        
        //update weights
        for ( int lSI = layers.size ( ) - 2; lSI > 0; lSI -- )
        {
            Layer currentLayer = layers.get ( lSI );
            Layer priorLayer = layers.get ( lSI - 1 );
            
            for ( int lI = 0; lI < priorLayer.size ( ) - 1; lI ++ )
            {
                currentLayer.get ( lI ).updateWeights ( priorLayer );
            }
        }    
   }
   
   
   //neural network outcome
   public double getOutcome ( )
   {
       Neuron outcomeNeuron = layers.get ( layers.size ( ) - 1 ).get ( 0 );
       
       return outcomeNeuron.getOutcome ( );
   }
}