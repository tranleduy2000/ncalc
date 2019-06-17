## KolmogorovSmirnovTest

```
KolmogorovSmirnovTest(data)
```

> Computes the `p-value`, or <i>observed significance level</i>, of a one-sample [Wikipedia:Kolmogorov-Smirnov test](http://en.wikipedia.org/wiki/Kolmogorov-Smirnov_test) evaluating the null hypothesis that `data` conforms to the `NormalDistribution()`.

```
KolmogorovSmirnovTest(data, distribution)
```

> Computes the `p-value`, or <i>observed significance level</i>, of a one-sample [Wikipedia:Kolmogorov-Smirnov test](http://en.wikipedia.org/wiki/Kolmogorov-Smirnov_test) evaluating the null hypothesis that `data` conforms to the (continuous) `distribution`.

```
KolmogorovSmirnovTest(data, distribution, "TestData")
```

> Computes the 	`test statistic` and the `p-value`, or <i>observed significance level</i>, of a one-sample [Wikipedia:Kolmogorov-Smirnov test](http://en.wikipedia.org/wiki/Kolmogorov-Smirnov_test) evaluating the null hypothesis that `data` conforms to the  (continuous) `distribution`.

### Examples

``` 
>> data = { 0.53236606, -1.36750258, -1.47239199, -0.12517888, -1.24040594, 1.90357309,
            -0.54429527, 2.22084140, -1.17209146, -0.68824211, -1.75068914, 0.48505896,
            2.75342248, -0.90675303, -1.05971929, 0.49922388, -1.23214498, 0.79284888,
            0.85309580, 0.17903487, 0.39894754, -0.52744720, 0.08516943, -1.93817962,
            0.25042913, -0.56311389, -1.08608388, 0.11912253, 2.87961007, -0.72674865,
            1.11510699, 0.39970074, 0.50060532, -0.82531807, 0.14715616, -0.96133601,
            -0.95699473, -0.71471097, -0.50443258, 0.31690224, 0.04325009, 0.85316056,
            0.83602606, 1.46678847, 0.46891827, 0.69968175, 0.97864326, 0.66985742, -0.20922486, -0.15265994}
            
>> KolmogorovSmirnovTest(data)
0.744855

>> KolmogorovSmirnovTest(data, NormalDistribution(), "TestData")
{0.0930213,0.744855}
```  

