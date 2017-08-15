## ToPolarCoordinates

```
ToPolarCoordinates({x, y})
```

> return the polar coordinates for the cartesian coordinates `{x, y}`.

```
ToPolarCoordinates({x, y, z})
```
 
> return the polar coordinates for the cartesian coordinates `{x, y, z}`.

### Examples

```
>> ToPolarCoordinates({x, y})
{Sqrt(x^2+y^2),ArcTan(x,y)}

>> ToPolarCoordinates({x, y, z})
{Sqrt(x^2+y^2+z^2),ArcCos(x/Sqrt(x^2+y^2+z^2)),ArcTan(y,z)}
```
  