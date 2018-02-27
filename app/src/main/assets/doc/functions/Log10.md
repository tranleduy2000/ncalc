## Log10

``` 
Log10(z)
``` 
> returns the base-`10` logarithm of `z`. `Log10(z)` will be converted to `Log(z)/Log(10)` in symbolic mode.

### Examples 
``` 
>> Log10(1000)    
3    
 
>> Log10({2., 5.})     
{0.30102999566398114,0.6989700043360186}
 
>> Log10(E ^ 3)    
3/Log(10)   
``` 