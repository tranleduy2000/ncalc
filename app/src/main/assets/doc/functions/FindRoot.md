## FindRoot

```
FindRoot(f, {x, xmin, xmax})
```

> searches for a numerical root of `f` for the variable `x`, in the range `xmin` to `xmax`. 

```
FindRoot(f, {x, xmin, xmax}, MaxIterations->maxiter)
```

> searches for a numerical root of `f` for the variable `x`, with `maxiter` iterations. The default maximum iteraton is `100`.

```
FindRoot(f, {x, xmin, xmax}, Method->method_name)
```

> searches for a numerical root of `f` for the variable `x`, with one of the following method names:


#### Newton

Implements Newton's method for finding zeros of real univariate functions.
The function should be continuous but not necessarily smooth. This is the default method, if no method name is given

#### Bisection

Implements the bisection algorithm for finding zeros of univariate real functions.
The function should be continuous but not necessarily smooth.

#### Brent

Implements the Brent algorithm for finding zeros of real univariate functions. 
The function should be continuous but not necessarily smooth. 
The solve method returns a zero `x` of the function `f` in the given interval `[xmin, xmax]` to within a 
tolerance `6*eps*abs(x)+t` where `eps` is the relative accuracy and `t` is the absolute accuracy. 
The given interval must bracket the root.

#### Muller

Implements the Muller's Method for root finding of real univariate functions. 
For reference, see Elementary Numerical Analysis, ISBN 0070124477, chapter 3.
Muller's method applies to both real and complex functions, but here we restrict ourselves to real functions. 
Muller's original method would have function evaluation at complex point. 
Since our `f(x)` is real, we have to find ways to avoid that. 
Bracketing condition is one way to go: by requiring bracketing in every iteration,
the newly computed approximation is guaranteed to be real.
Normally Muller's method converges quadratically in the vicinity of a zero, 
however it may be very slow in regions far away from zeros. 
For example, `FindRoot(Exp(x)-1 == 0,{x,-50,100}, Method->Muller)`. 
In such case we use bisection as a safety backup if it performs very poorly.
The formulas here use divided differences directly.

#### Ridders

Implements the Ridders' Method for root finding of real univariate functions. 
For reference, see C. Ridders, A new algorithm for computing a single root of a real continuous function, 
IEEE Transactions on Circuits and Systems, 26 (1979), 979 - 980.
The function should be continuous but not necessarily smooth.

#### Secant

Implements the Secant method for root-finding (approximating a zero of a univariate real function). 
The solution that is maintained is not bracketed, and as such convergence is not guaranteed.

#### RegulaFalsi

Implements the Regula Falsi or False position method for root-finding (approximating a zero of a univariate real function). It is a modified Secant method.
The Regula Falsi method is included for completeness, for testing purposes, for educational purposes, for comparison to other algorithms, etc. It is however not intended to be used for actual problems, as one of the bounds often remains fixed, resulting in very slow convergence. Instead, one of the well-known modified Regula Falsi algorithms can be used (Illinois or Pegasus). These two algorithms solve the fundamental issues of the original Regula Falsi algorithm, and greatly out-performs it for most, if not all, (practical) functions.
Unlike the Secant method, the Regula Falsi guarantees convergence, by maintaining a bracketed solution. Note however, that due to the finite/limited precision of Java's double type, which is used in this implementation, the algorithm may get stuck in a situation where it no longer makes any progress. Such cases are detected and result in a ConvergenceException exception being thrown. In other words, the algorithm theoretically guarantees convergence, but the implementation does not.
The Regula Falsi method assumes that the function is continuous, but not necessarily smooth.

#### Illinois

Implements the Illinois method for root-finding (approximating a zero of a univariate real function). It is a modified Regula Falsi method.
Like the Regula Falsi method, convergence is guaranteed by maintaining a bracketed solution. The Illinois method however, should converge much faster than the original Regula Falsi method. Furthermore, this implementation of the Illinois method should not suffer from the same implementation issues as the Regula Falsi method, which may fail to convergence in certain cases.
The Illinois method assumes that the function is continuous, but not necessarily smooth.

#### Pegasus

Implements the Pegasus method for root-finding (approximating a zero of a univariate real function). 
It is a modified Regula Falsi method. Like the Regula Falsi method, convergence is guaranteed by maintaining a bracketed solution. 
The Pegasus method however, should converge much faster than the original Regula Falsi method. 
The Pegasus method should converge faster than the Illinois method, another Regula Falsi-based method.
The Pegasus method assumes that the function is continuous, but not necessarily smooth. 

### Examples 
```
>> FindRoot(Exp(x)==Pi^3,{x,-1,10}, Method->Bisection)
{x->3.434189647436142}

>> FindRoot(Sin(x), {x, -0.5, 0.5})
{x->0.0} 
```

### Related terms 
[Factor](Factor.md), [Eliminate](Eliminate.md), [NRoots](NRoots.md), [Solve](Solve.md)