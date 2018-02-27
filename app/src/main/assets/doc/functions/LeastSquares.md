## LeastSquares
```
LeastSquares(matrix, right)
```

> solves the linear least-squares problem 'matrix . x = right'.

### Examples	
```
>> LeastSquares(Table(Complex(i,Rational(2 * i + 2 + j, 1 + 9 * i + j)),{i,0,3},{j,0,2}), {1,1,1,1})
{-1577780898195/827587904419-I*11087326045520/827587904419,35583840059240/5793115330933+I*275839049310660/5793115330933,-3352155369084/827587904419-I*2832105547140/827587904419}
```
 