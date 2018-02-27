## Median

```
Median(list)
```

> returns the median of `list`. 
  
See:
* [Wikipedia - Median](https://en.wikipedia.org/wiki/Median)

### Examples

``` 
>> Median({26, 64, 36})
36
```

For lists with an even number of elements, Median returns the mean of the two middle values:

```
>> Median({-11, 38, 501, 1183})
539/2
```

Passing a matrix returns the medians of the respective columns:

```
>> Median({{100, 1, 10, 50}, {-1, 1, -2, 2}})
{99/2,1,4,26}
```  

