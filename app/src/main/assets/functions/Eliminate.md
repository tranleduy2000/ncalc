## Eliminate 

```
Eliminate(list-of-equations, list-of-variables)
```

> attempts to eliminate the variables from the `list-of-variables` in the `list-of-equations`.

### Examples 
```
>>> Eliminate({x==2+y, y==z}, y)
x==2+z
```
