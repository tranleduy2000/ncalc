## FromPolarCoordinates

```
FromPolarCoordinates({r, t})
```

> return the cartesian coordinates for the polar coordinates `{r, t}`.

```
FromPolarCoordinates({r, t, p})
```
 
> return the cartesian coordinates for the polar coordinates `{r, t, p}`.

### Examples

```
>> FromPolarCoordinates({r, t})
{r*Cos(t),r*Sin(t)}

>> FromPolarCoordinates({r, t, p})
{r*Cos(t),r*Cos(p)*Sin(t),r*Sin(p)*Sin(t)}
```
  