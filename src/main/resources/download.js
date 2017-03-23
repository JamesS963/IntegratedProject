
$(document).ready(function(){
    $("h1").click(function(){
        $(this).hide();
    });

    //$.getJSON("http://localhost:8080/download", function(data){
        var documents=[];

        var testData=[
        {name:"James",revision:12,link:"your mum"},
        {name:"Eve",revision:22,link:"your cats sister"},
        {name:"Noah",revision:34,link:"The ark has failed us"}

        ];

        var wrapperDiv = $("<div />", {"class": "wrapper"});
        $.each(testData, function(key,val){

           var doc=$("<div />");

           var name=$("<p />", {text:testData[key].name});
           var revision=$("<p />", {text:testData[key].revision});
           var link=$("<p />", {text:testData[key].link});
           doc.append(name,revision,link);
           wrapperDiv.append(doc);

        });
        $("body").append(wrapperDiv);
   //});


   /*
    function(data){
    $.each(data.products, function(i,product){
            content = '<p>' + product.product_title + '</p>';
            content += '<p>' + product.product_short_description + '</p>';
            content += '<img src="' + product.product_thumbnail_src + '"/>';
            content += '<br/>';
            $(content).appendTo("#product_list");
          });

          */
});