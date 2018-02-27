## YuleDissimilarity

```
YuleDissimilarity(u, v)
```

> returns the Yule dissimilarity between the two boolean 1-D lists `u` and `v`, which is defined as R / (c_tt * c_ff + R / 2) where n is `len(u)`, `c_ij` is the number of occurrences of `u(k)=i` and `v(k)=j` for `k<n`, and `R = 2 * c_tf * c_ft`.
  
### Examples

```
>> YuleDissimilarity({1, 0, 1, 1, 0, 1, 1}, {0, 1, 1, 0, 0, 0, 1})
6/5
```