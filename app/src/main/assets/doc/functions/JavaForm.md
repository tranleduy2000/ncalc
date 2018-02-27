## JavaForm

```
JavaForm(expr)
```

> returns the Symja Java form of the `expr`. In Java you can use the created Symja expressions.

### Examples

JavaForm can add the `F.` prefix for class `org.matheclipse.core.expression.F` if you set `prefix->True`:

```
>> JavaForm(D(sin(x)*cos(x),x), prefix->True)
"F.Plus(F.Sqr(F.Cos(F.x)),F.Negate(F.Sqr(F.Sin(F.x))))"

>> JavaForm(I/2*E^((-I)*x)-I/2*E^(I*x))
"Plus(Times(CC(0L,1L,1L,2L),Power(E,Times(CNI,x))),Times(CC(0L,1L,-1L,2L),Power(E,Times(CI,x))))"
```

JavaForm evaluates its argument before creating the Java form:

```
>> JavaForm(D(sin(x)*cos(x),x))
"Plus(Sqr(Cos(x)),Negate(Sqr(Sin(x))))"
```

You can use `Hold` to suppress the evaluation:

```
>> JavaForm(Hold(D(sin(x)*cos(x),x)))
"D(Times(Sin(x),Cos(x)),x)"

>> JavaForm(Hold(D(sin(x)*cos(x),x)), prefix->True)
"F.D(F.Times(F.Sin(F.x),F.Cos(F.x)),F.x)"
```
 