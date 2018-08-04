## OptimizeExpression

```
OptimizeExpression(function)
```

> common subexpressions elimination for a complicated `function` by generating "dummy" variables for these subexpressions.
 
## Examples


```
>> OptimizeExpression( Sin(x) + Cos(Sin(x)) )
{v1+Cos(v1),{v1->Sin(x)}}

>> OptimizeExpression((3 + 3*a^2 + Sqrt(5 + 6*a + 5*a^2) + a*(4 + Sqrt(5 + 6*a + 5*a^2)))/6)
{1/6*(3+3*v1+v2+a*(4+v2)),{v1->a^2,v2->Sqrt(5+6*a+5*v1)}}
```

Create the original expression:

```
>> ReplaceRepeated(1/6*(3+3*v1+v2+a*(4+v2)), {v1->a^2, v2->Sqrt(5+6*a+5*v1)})
1/6*(3+3*a^2+Sqrt(5+6*a+5*a^2)+a*(4+Sqrt(5+6*a+5*a^2)))
```
