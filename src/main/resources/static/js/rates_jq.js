$(document).ready(function(){
   let from = "";
   let to = "";
   let fromValue = "";

   $('#inlineFormCustomSelect').change(function () {
      callRates().then(value => {
         $("#convertedVal").val(value.convertedValue);
      })
   })

   $('#inlineFormCustomSelect2').change(function () {
      callRates().then(value => {
         $("#convertedVal").val(value.convertedValue);
      })
   })

   $("#baseCurrencyInput").on("change paste keyup", function() {
       callRates().then(value => {
           $("#convertedVal").val(value.convertedValue);
       })
   });

   async function callRates() {
      from = $('#inlineFormCustomSelect option:selected').val()
      to = $('#inlineFormCustomSelect2 option:selected').val()
      fromValue = $("#baseCurrencyInput").val();
      if(from !== "" && to !== "" && from != to && fromValue != ""){
        const val = await $.ajax({
            type: "GET",
            url: "/feed/V1/rates",
            contentType: "application/json",
            data: {
               from: from,
               to: to,
               toConvertValue: fromValue
            }

         }).fail(() => {});
         return val;
      }else if(from == to) {
          $("#convertedVal").val(fromValue);
          await Promise.reject();
      }
   }
});