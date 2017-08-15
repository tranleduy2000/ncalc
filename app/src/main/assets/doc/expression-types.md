## Expression types

| Type          		| Description         																							| Input Example 						|
|:---------------------:|:-------------------------------------------------------------------------------------------------------------:|:-------------------------------------:|
| Integer				| integer numbers   																							| `42`  								|
| Rational				| rational numbers        																						| `13/17` 								|
| Complex				| complex numbers      			 																				| `2+I*1/3`    							|
| Real					| double values  																								| `0.5`  								|
| Complex Real			| complex double values  																						| `0.5-I*0.25`  						|
| Evaluation Precedence	| control precedence with `(...)`  																				| `(a+b)*c`  							|
| Lists					| comma separated list of elements which are surrounded by `{ ... }`  											| `{a, b, c, d} `  						|
| Vectors				| vectors are like list, but cannot contain sub-lists `{ ... }`  												| `{1, 2, 3, 4}`  						|
| Matrices				| a matrix contains the rows as sub-lists 																		| `{{1, 2}, {3, 4}}`  					|
| Predefined Functions	| predefined function names start with an upper case character and the arguments are enclosed by `( ... )`	| `Sin(0), PrimeQ(13)` 					|
| Predefined Constants	| predefined constant names start with an upper case character 													| `Degree, E, Pi, False, True, ... `	|
| User-defined variables| identifiers which you would like to assign a value start with a `$` character in the server environment		| `$a=42`  								|
| User-defined variables| in the Symja console app user-defined variables can be defined without a preceding `$` character				| `a=42`  								|
| User-defined rules	| identifiers which you would like to assign a rule start with a `$` character in the server environment		| `$f(x_,y_):={x,y}`  					|
| User-defined rules	| in the Symja console app user-defined rules can be defined without a preceding `$` character					| `f(x_,y_):={x,y}`  
| Pattern Symbols		| patterns end with an appended `_` character and could have a constraint 										| `$f(x_Integer):={x}`  				|
| Strings				| character strings are enclosed by double quote characters  													| `"Hello World"`  						|
| Slots					| a `#` character followed by an optional integer number 														| `#` or `#2`   						|
| Pure Functions		| pure functions can be expressed with the `&` operator															| `(#^3)&[x]`  gives `x^3` 				|
| Parts of an expression| `expr[[index]]`   																							| `{a, b, c, d}[[2]]`  gives `b`		|
