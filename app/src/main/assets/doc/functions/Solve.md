## Solve 

```
Solve(equations, vars)
```

> attempts to solve `equations` for the variables `vars`.

```
Solve(equations, vars, domain)
```

> attempts to solve `equations` for the variables `vars` in the given `domain`.

### Examples 

```
>> Solve({x^2==4,x+y^2==6}, {x,y})
{{x->2,y->2},{x->2,y->-2},{x->-2,y->2*2^(1/2)},{x->-2,y->(-2)*2^(1/2)}}

>> Solve({2*x + 3*y == 4, 3*x - 4*y <= 5,x - 2*y > -21}, {x,  y}, Integers)
{{x->-7,y->6},{x->-4,y->4},{x->-1,y->2}}

>> Solve(Xor(a, b, c, d) && (a || b) && ! (c || d), {a, b, c, d}, Booleans)
{{a->False,b->True,c->False,d->False},{a->True,b->False,c->False,d->False}}
```

### Related terms 
[DSolve](DSolve.md), [Eliminate](Eliminate.md), [GroebnerBasis](GroebnerBasis.md), [FindInstance](FindInstance.md), [FindRoot](FindRoot.md), [NRoots](NRoots.md), [NSolve](NSolve.md)