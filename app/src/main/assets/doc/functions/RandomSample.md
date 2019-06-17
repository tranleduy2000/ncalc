## RandomSample

```
RandomSample(<function>)
```

> create a random sample for the arguments of the `function`.

```
RandomSample(<function>, n)
```

> create a random sample of `n` elements for the arguments of the `function`.

### Examples

```
>> RandomSample(f(1,2,3,4,5))
f(3,4,5,1,2)

>> RandomSample(f(1,2,3,4,5),3)
f(3,4,1)
```