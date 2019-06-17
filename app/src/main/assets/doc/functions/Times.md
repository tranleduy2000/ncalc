## Times

```
Times(a, b, ...)

a * b * ...
```

> represents the product of the terms `a, b, ...`.

**Note**: the `Times` operator has the attribute `Flat` ([associative property](https://en.wikipedia.org/wiki/Associative_property)), `Orderless` ([commutative property](https://en.wikipedia.org/wiki/Commutative_property)), `OneIdentity` and `Listable`.

### Examples

```
>> 10*2
20

>> a * a
a^2

>> x ^ 10 * x ^ -2
x^8

>> {1, 2, 3} * 4
{4,8,12}

>> Times @@ {1, 2, 3, 4}
24

>> IntegerLength(Times@@Range(100))
158
```

`Times` has default value `1`:

```
>> a /. n_. * x_ :> {n, x}
{1,a}

>> -a*b // FullForm
"Times(-1, a, b)"

>> -(x - 2/3)
2/3-x

>> -x*2
-2 x

>> -(h/2) // FullForm
"Times(Rational(-1,2), h)"

>> x / x
1

>> 2*x^2 / x^2
2

>> 3.*Pi
9.42477796076938

>> Head(3 * I)
Complex

>> Head(Times(I, 1/2))
Complex

>> Head(Pi * I)
Times

>> -2.123456789 * x
-2.123456789*x

>> -2.123456789 * I
I*(-2.123456789)

>> N(Pi, 30) * I
I*3.14159265358979323846264338327

>> N(I*Pi, 30)
I*3.14159265358979323846264338327

>> N(Pi * E, 30)
8.53973422267356706546355086954

>> N(Pi, 30) * N(E, 30)
8.53973422267356706546355086954

>> N(Pi, 30) * E
8.53973422267356649108017774746

>> N(Pi, 30) * E // Precision
30
```

### Related terms
[Flat](Flat.md), [Listable](Listable.md), [MatrixPower](MatrixPower.md), [OneIdentity](OneIdentity.md), [Orderless](Orderless.md), [Power](Power.md)
