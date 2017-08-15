## NIntegrate
```
NIntegrate(f, {x,a,b})
```
> computes the numerical univariate real integral of `f` with respect to `x` from `a` to `b`.

### Examples
```   
>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1})
-0.0208333333333333
```

LegendreGauss is the default method for numerical integration
```
>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1}, Method->LegendreGauss)
-0.0208333333333333

>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1}, Method->Simpson)
-0.0208333320915699

>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1}, Method->Trapezoid)
-0.0208333271245165

>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1}, Method->Romberg)
-0.0208333333333333
```

Other options include `MaxIterations` and `MaxPoints`
```
>> NIntegrate((x-1)*(x-0.5)*x*(x+0.5)*(x+1), {x,0,1}, Method->Trapezoid, MaxIterations->5000)
-0.0208333271245165
```
		