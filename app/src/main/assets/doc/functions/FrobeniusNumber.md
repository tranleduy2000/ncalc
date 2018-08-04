## FrobeniusNumber

```
FrobeniusNumber({a1, ... ,aN})
```

> returns the Frobenius number of the nonnegative integers `{a1, ... ,aN}`

The Frobenius problem, also known as the postage-stamp problem or the money-changing problem, is an integer programming problem that seeks nonnegative integer solutions to `x1*a1 + ... + xN*aN = M` where `ai` and `M` are positive integers.
In particular, the Frobenius number `FrobeniusNumber({a1, ... ,aN})`, is the largest `M` so that this equation fails to have a solution.

See: 
* [Wikipedia - Coin problem](https://en.wikipedia.org/wiki/Coin_problem)

### Examples

```
>> FrobeniusNumber({1000, 1476, 3764, 4864, 4871, 7773})
47350
```