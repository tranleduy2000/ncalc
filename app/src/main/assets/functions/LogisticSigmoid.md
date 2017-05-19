## LogisticSigmoid

``` 
LogisticSigmoid(z)
``` 
> returns the logistic sigmoid of `z`.

### Examples 
```  
>> LogisticSigmoid(0.5)
0.6224593312018546
 
>> LogisticSigmoid(0.5 + 2.3*I)
1.0647505893884985+I*0.8081774171575826
 
>> LogisticSigmoid({-0.2, 0.1, 0.3})
{0.45016600268752216,0.52497918747894,0.574442516811659} 
 
>> LogisticSigmoid(I*Pi)
LogisticSigmoid(I*Pi)
``` 