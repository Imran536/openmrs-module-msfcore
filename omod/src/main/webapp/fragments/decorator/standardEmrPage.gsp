<%
    sessionContext.requireAuthentication()
    ui.includeFragment("appui", "standardEmrIncludes")
    def title = config.title ?: ui.message("emr.title")
    def timezoneOffset = -Calendar.getInstance().getTimeZone().getOffset(System.currentTimeMillis()) / 60000
    def jsTimezone = new java.text.SimpleDateFormat("ZZ").format(new Date());
%>

<!DOCTYPE html>
<html>
    <head>
        <title>${ title ?: "OpenMRS" }</title>

        <!-- MSF: customize favicon -->
        <link rel="shortcut icon" type="image/ico" href="${ui.resourceLink('msfcore', 'images/msf_favicon.ico')}"/>
        <link rel="icon" type="image/png\" href="${ui.resourceLink('msfcore', 'images/msf_favicon.png')}"/>
        <!-- /MSF: customize favicon -->

        <% ui.includeCss("appui", "header.css") %>
        ${ ui.resourceLinks() }
        <!-- MSF: We may override the header or else just style it here -->
        <link href="${ui.resourceLink('msfcore', 'styles/msf.css')}" rel="stylesheet" type="text/css" media="all">
        <!-- /MSF: We may override the header or else just style it here -->
    </head>
    <body>
        <script type="text/javascript">
            var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
            var openmrsContextPath = '/' + OPENMRS_CONTEXT_PATH;
            window.sessionContext = window.sessionContext || {
                locale: "${ ui.escapeJs(sessionContext.locale.toString()) }"
            };
            window.translations = window.translations || {};
            var openmrs = {
                server: {
                    timezone: "${ jsTimezone }",
                    timezoneOffset: ${ timezoneOffset }
                }
            }
        </script>

${ ui.includeFragment("appui", "header") }

<ul id="breadcrumbs"></ul>

<div id="body-wrapper">

    ${ ui.includeFragment("uicommons", "infoAndErrorMessage") }

    <div id="content" class="container">
        <%= config.content %>
    </div>

</div>

<script id="breadcrumb-template" type="text/template">
    <li>
        {{ if (!first) { }}
        <i class="icon-chevron-right link"></i>
        {{ } }}
        {{ if (!last && breadcrumb.link) { }}
        <a href="{{= breadcrumb.link }}">
        {{ } }}
        {{ if (breadcrumb.icon) { }}
        <i class="{{= breadcrumb.icon }} small"></i>
        {{ } }}
        {{ if (breadcrumb.label) { }}
        {{= breadcrumb.label }}
        {{ } }}
        {{ if (!last && breadcrumb.link) { }}
        </a>
        {{ } }}
    </li>
</script>

<script type="text/javascript">
    jq(function() {
        emr.updateBreadcrumbs();
    });
    // global error handler
    jq(document).ajaxError(function(event, jqxhr) {
        emr.redirectOnAuthenticationFailure(jqxhr);
    });
    var featureToggles = {};
    <% featureToggles.getToggleMap().each { %>
        featureToggles["${it.key}"] = ${ Boolean.parseBoolean(it.value)};
    <% } %>

    jq(function() {
        // remove the second edit link on patient dashboard
        jq("#contact-info-inline-edit").detach();

        // Update the main edit link
        var extractPatientIdFromUrl = function(url) {
            if (url && url.startsWith("/openmrs/registrationapp/editSection.page?patientId=")) {
                return url.split("?")[1].split("&")[0].split("=")[1];
            } else {
                return null;
            }
        }

        var previousUrl = jq("#edit-patient-demographics > small > a").attr("href");
        var patientId = extractPatientIdFromUrl(previousUrl);
        if (patientId) {
            var newUrl = "/openmrs/registrationapp/registerPatient.page?appId=msfcore.registrationapp&patientId=" + patientId;
            jq("#edit-patient-demographics > small > a").attr("href", newUrl);
        }
    });
</script>

    </body>
</html>

<!-- MSF: Do we need an MSF footer? -->