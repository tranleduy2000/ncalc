## Complex

```
Complex
```

> is the head of complex numbers.

```
Complex(a, b)
```

> constructs the complex number `a + I * b`.


### Examples
```
>> Head(2 + 3*I)
Complex

>> Complex(1, 2/3)
1+I*2/3

>> Abs(Complex(3, 4))
5

>> -2 / 3 - I
-2/3-I

>> Complex(10, 0)
10

>> 0. + I
I*1.0

>> 1 + 0*I
1

>> Head(1 + 0*I)
Integer

>> Complex(0.0, 0.0)
0.0

>> 0.*I
0.0

>> 0. + 0.*I
0.0

>> 1. + 0.*I
1.0

>> 0. + 1.*I
I*1.0
```

Check Nesting Complex
```
>> Complex(1, Complex(0, 1))
0

>> Complex(1, Complex(1, 0))
1+I 

>> Complex(1, Complex(1, 1))
I
```