<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeCss("referenceapplication", "login.css")
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ ui.message("referenceapplication.login.title") }</title>
    
    <!-- MSF: customize favicon -->
        <link rel="shortcut icon" type="image/ico" href="${ui.resourceLink('msfcore', 'images/msf_favicon.ico')}"/>
        <link rel="icon" type="image/png\" href="${ui.resourceLink('msfcore', 'images/msf_favicon.png')}"/>
    <!-- /MSF: customize favicon -->
    
	${ ui.resourceLinks() }
    <!-- MSF: We may override the header or else just style it here -->
        <link href="${ui.resourceLink('msfcore', 'styles/msf.css')}" rel="stylesheet" type="text/css" media="all">
    <!-- /MSF: We may override the header or else just style it here -->
</head>
<body id="login">
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>

<script type="text/javascript">
    jQuery(function() {
    	updateSelectedOption = function() {
	        jQuery('#sessionLocation li').removeClass('selected');
	        
			var sessionLocationVal = jQuery('#sessionLocationInput').val();
	        if(sessionLocationVal != null && sessionLocationVal != "" && sessionLocationVal != 0){
	            jQuery('#sessionLocation li[value|=' + sessionLocationVal + ']').addClass('selected');
	        }
    	};
    
        updateSelectedOption();
        jQuery('#sessionLocation li').click( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });
        
        jQuery('#login-form').submit(function(e) {
        	var sessionLocationVal = jQuery('#sessionLocationInput').val();
        	
        	if (!sessionLocationVal) {
       			jQuery('#sessionLocationError').show(); 		
        		e.preventDefault();
        	}
        });	
        
        jQuery('#username').focus();
        var cannotLoginController = emr.setupConfirmationDialog({
            selector: '#cannotLoginPopup',
            actions: {
                confirm: function() {
                    cannotLoginController.close();
                }
            }
        });
        
        jQuery('a#cantLogin').click(function() {
            cannotLoginController.show();
        });
        
        pageReady = true;
    });
</script>

<header>
    <div class="logo">
        <img src="${ui.resourceLink("msfcore", "images/msf_logo.png")}"/>
    </div>
</header>

<div id="body-wrapper">
    <div id="content">

        <form id="login-form" class="content-box" method="post" autocomplete="off">
            <div class="flex-row flex-center bdr-btm-light margin-t-0 margin-b-32">
                <i class="icon-lock txt-size-3x margin-r-8"></i>
                <h1>${ ui.message("referenceapplication.login.loginHeading") }</h1>
            </div>

            <fieldset class="flex-col">
                ${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }

                <div class="flex-col margin-b-16">
                    <label for="username">
                        ${ ui.message("referenceapplication.login.username") }:
                    </label>
                    <input id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                </div>

                <div class="flex-col margin-b-16">
                    <label for="password">
                        ${ ui.message("referenceapplication.login.password") }:
                    </label>
                    <input id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                </div>

                <label for="sessionLocation">
                    ${ ui.message("referenceapplication.login.sessionLocation") }: <span class="location-error" id="sessionLocationError" style="display: none">${ui.message("referenceapplication.login.error.locationRequired")}</span>
                </label>
                <ul id="sessionLocation" class="select">
                    <% locations.sort { ui.format(it) }.each { %>
                    <li id="${it.name}" value="${it.id}">${ui.format(it)}</li>
                    <% } %>
                </ul>

                <input type="hidden" id="sessionLocationInput" name="sessionLocation"
                    <% if (lastSessionLocation != null) { %> value="${lastSessionLocation.id}" <% } %> />

                <div class="margin-t-32 text-center">
                    <button class="btn-block" type="submit">${ ui.message("referenceapplication.login.button") }</button>
                </div>
                <div class="text-center">
                    <a href="javascript:void(0)">
                        <i class="icon-question-sign small"></i>
                        ${ ui.message("referenceapplication.login.cannotLogin") }
                    </a>
                </div>

            </fieldset>

    		<input type="hidden" name="redirectUrl" value="${redirectUrl}" />

        </form>

    </div>
</div>

<div id="cannotLoginPopup" class="dialog" style="display: none">
    <div class="dialog-header">
        <i class="icon-info-sign"></i>
        <h3>${ ui.message("referenceapplication.login.cannotLogin") }</h3>
    </div>
    <div class="dialog-content">
        <p class="dialog-instructions">${ ui.message("referenceapplication.login.cannotLoginInstructions") }</p>

        <button class="confirm">${ ui.message("referenceapplication.okay") }</button>
    </div>
</div>

</body>
</html>