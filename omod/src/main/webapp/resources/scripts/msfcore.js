jQuery(document).ready(function() {

    function getParameterByName( name ){
        var regexS = "[\\?&]"+name+"=([^&#]*)", 
      regex = new RegExp( regexS ),
      results = regex.exec( window.location.search );
      if( results == null ){
        return "";
      } else{
        return decodeURIComponent(results[1].replace(/\+/g, " "));
      }
    }

    // Add the patient details view
    var url = window.location.href;
    var arr = url.split("/");
    var serverPath = arr[0] + "//" + arr[2] + "/" + arr[3];
    jQuery('.identifiers').after("<div id='patient_details' class='hidden'></div>");
    jQuery('#patient_details').load(serverPath + "/msfcore/patientDetails.page?patientId=" + getParameterByName('patientId'));
    jQuery('.identifiers').prepend("<span id='info_patient_detail' style='cursor:pointer'><small>INFO</small></span>");
    
    jQuery('#info_patient_detail').click(function() {
        if (jQuery('#patient_details').is(':visible')) {
            jQuery('#patient_details').hide();
        } else {
            jQuery('#patient_details').show();
        }
    })

    // Patient Search widget handling
    if (jQuery('#patient-search-register-patient').length == 1) {
        // add a header section just above the form
        // Hide the register patient link
        jQuery('#register-patient-link').hide();
        // remove the page heading
        jQuery('#patient-search-form').prevAll('h2').remove();

        // change the text on the register patient link
        jQuery('#patient-search-register-patient').html('+ ADD NEW PATIENT');
        var content = '<div id="page-header"><h2>Search for Patient Record</h2> ' +
            '               <a class="button" id="find-patient-back" href="#" onclick="window.history.go(-1); return false;">< Back</a> '
                            + jQuery('#patient-search-register-patient')[0].outerHTML + '</div>'

        // add the patient header contents
        jQuery('#patient-search-form').before(content);
    }

    // form handling functions
    jq(".enableDisable, .allergy").each(function(){
        disableFn(jq(this));
        jq(this).children("#trigger").find(":checkbox:first").change(function(){
            var checked = jq(this).attr("checked");
            if(checked == true){
                enableFn(jq(this));
            }else{
                disableFn(jq(this));
            }
        });
    });


    jq(".checkboxGroup").each(function(){
        var group = jq(this);
        var uncheckAll = function(){
            group.find("input[type$='checkbox']").attr("checked",false);
            group.find("input[type$='checkbox']").change();
        }
        var uncheckRadioAndAll = function(){
            group.find("#checkboxAll,#checkboxRadio").find("input[type$='checkbox']").attr("checked",false);
            group.find("#checkboxAll,#checkboxRadio").find("input[type$='checkbox']").change();
        }

        group.find("#checkboxAll").find("input").click(
            /*  This was tricky... A number of things needed to happen
               Basically, This is supposed to treat a group of inputs as if
               were all one big checkbox. It is designed so that a checkbox
                can be next to an input, and the user clicks the input, the
               checkbox checks as well. But, when the user clicks the checkbox,
                the browser marks the checkbox as checked. Therefore, when we check
                if the checkbox is already checked, it always respondes true...
                We needed to have 2 cases: when the clicking action is on the first checkbox
                and when the action is on any other.  */
            function(){
                var flip;
                var checked = jQuery(this).siblings(":checkbox:first").attr("checked");
                if(jq(this).attr("name") == jq(this).parents("#checkboxAll:first").find(":checkbox:first").attr("name")){
                    checked = jq(this).attr("checked");
                    flip = checked;
                }else{
                    flip = !checked;
                }
                if(jq(this).attr("type") == "text") if(flip == false) flip = !filp; // this is so the user doesn't go to check the checkbox, then uncheck it when they hit the input.
                uncheckAll();
                jq(this).parents("#checkboxAll:first").find(":checkbox").attr("checked",flip);
                jq(this).parents("#checkboxAll:first").find(":checkbox").change();
            }
        );

        group.find("#checkboxRadio").find("input[type$='checkbox']").click(function(){
            uncheckAll();
            jq(this).siblings("input[type$='checkbox']").attr("checked",false);
            jq(this).attr("checked",true);
            jq(this).change();
        });

        group.find("#checkboxCheckbox").click(
            function(){
                uncheckRadioAndAll();
            }
        );
    });
});

function disableFn(group){
    jq(group).addClass('hidden');
    jq(group).children("#disabled").fadeTo(250,0.33);
    jq(group).children("#disabled").find(":checkbox").attr("checked",false); //uncheck
    jq(group).children("#disabled").find("input[type$='text']").val("");
    jq(group).children("#disabled").find("input").attr("disabled",true);  //disable
}
function enableFn(group){
    jq(group).removeClass('hidden');
    jq(group).children("#disabled").fadeTo(250,1);
    jq(group).children("#disabled").find("input").attr("disabled",false);
}

function validateRequiredFields(requiredFieldIds, requiredMessage) {
    var allValid = true;
    var allRequiredFieldsAreMissing = true;
    requiredFieldIds.forEach(function(id) {
        var valid = validateRequiredField(id, requiredMessage);
        if (valid) {
            allRequiredFieldsAreMissing = false;
        }
        allValid = allValid && valid;
    });
    return allValid || allRequiredFieldsAreMissing;
}

function validateRequiredField(id, requiredMessage) {
    if (getValue(`${id}.value`) == '') {
        getField(`${id}.error`).html(requiredMessage).show();
        return false;
    } else {
        getField(`${id}.error`).html('').show();
        return true;
    }
}

