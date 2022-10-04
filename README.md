# presentation-currency-conversion-app
Spring-boot 2.7.3 🚀 + Thymeleaf ☘️  application for demo purposes.

## Motivation 💡
Learn reactive programming

## How to run 🏃

### Not yet containerized :p

### So in the hard way or deploy as a war file.

visit ➙ [http://localhost:8080](http://localhost:8080)

# How the conversion app working

1. The conversion app is fully reactive. 
2. It has a facade layer which abstracts the usage and will help to extend support for another conversion api in the future
3. All the conversions performed in big decimal.
4. Calls to the external api's are cached for 30 mins. As of now with reactive we cannot use Cache from spring boot 
   the app will get blocked which is not the best way. Bt still we have reactor addon based cachemanagers and in 
   this project am using GuavaBased Reactive cache layer.
5. Rest exceptions are handled in the end from the advisor after its been thrown from the controller.   
6. Why not use view model to populate symbol values 

  Although below way we can make the ui reactive through model object. 

    IReactiveDataDriverContextVariable reactiveDataDrivenMode =
              new ReactiveDataDriverContextVariable(exchangeServiceFacade.getExchangeRateSymbols(), 200);

 It doesn't work when trying to subscribe for the same flux stream twice. 
 It works for one fine but completely blocks the ui and doesn't render anything on the second currency drop down
 
# UnCompletedTask 
 1. Unit test in the service layer.