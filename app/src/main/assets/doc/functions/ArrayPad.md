## ArrayPad 

```
ArrayPad(list, n)
```

> adds `n` times `0` on the left and right of the `list`. 


```
ArrayPad(list, {m,n})
```

> adds `m` times `0` on the left and `n` times `0` on the right. 

```
ArrayPad(list, {m, n}, x)
```

> adds `m` times `x` on the left and `n` times `x` on the right. 

### Examples 

```
>> ArrayPad({a, b, c}, 1, x)
{x,a,b,c,x}
```


