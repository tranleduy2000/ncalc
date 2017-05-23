## Solve 

```
Solve(equations, vars)
```

> attempts to solve `equations` for the variables `vars`.

### Examples 
```
>> Solve({x^2==4,x+y^2==6}, {x,y})
{{x->2,y->2},{x->2,y->-2},{x->-2,y->2*2^(1/2)},{x->-2,y->(-2)*2^(1/2)}}
```
