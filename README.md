# pava
[github link](https://github.com/fightinggg/compiler)

[pdk in dockerhub link](https://hub.docker.com/r/fightinggg/pdk)

[pre in dockerhub link](https://hub.docker.com/r/fightinggg/pre)

## pdk1.0.0 preview
what is pdk ?

pdk is a pava language development kits, in pdk , you can use command `pava` and command `pavac`

step1. run pdk container
```shell
$ docker run -it fightinggg/pdk:1.0.0 sh
```

step2. look the pava code
```shell
$ cat code.pava
int fib(int x){
    if(x<2) return 1;
    return fib(x-1) + fib(x-2);
}
int main(){
    int a = fib(5);
    return a;
}
```

step3. look the pavac
```shell
$ pavac
```
then you can see this output
```txt
welcome to pavac 1.0 ...
you can compile pavac code like this :
$ pavac -pava code.pava
you can debug pava code like this :
$ pavac -pava code.pava -debug
you can output to file like this :
$ pavac -pava code.pava -output code.par
```

step4. compile pava code using pavac
```shell
$ pavac -pava code.pava -output code.par
```

step5. look the PVM (Pava Virtual Machine)
```shell
$ pava
```
then you can see this output 
```txt
welcome to pava 1.0 ...
you can run pava code like this :
$ pava -par code.par
you can debug pava code like this :
$ pava -par code.par -debug
```

step6. run par code using Pava Virtual Machine
```shell
$ pava -par code.par
```
then you can see the output 
```txt
return: 8
```

so , is it amazing ? the code code.pava is a pava code and it compute the fifth Fibonacci Number,

Fibonacci Number is 1 2 3 5 8 13 21 34 ...

enjoy the pdk 1.0.0 and pre 1.0.0

for more study about pdk , just see [fightinggg's blog](https://fightinggg.github.io/QV7MPO.html)