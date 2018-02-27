## Fit  

``` 
Fit(list-of-points, degree, variable)
```  
 
> solve a least squares problem using the Levenberg-Marquardt algorithm.
   
See:  
* [Wikipedia - Levenberg–Marquardt algorithm](http://en.wikipedia.org/wiki/Levenberg%E2%80%93Marquardt_algorithm) 
 
### Examples  
``` 
>> Fit({{1,1},{2,4},{3,9},{4,16}},2,x)
x^2.0
```