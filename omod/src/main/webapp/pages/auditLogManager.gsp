<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("msfcore.auditlogs.manager.label") ])
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("msfcore.auditlogs.manager.label")}"}
    ];
</script>

<% ui.includeJavascript("uicommons", "angular.min.js") %>
<% ui.includeJavascript("msfcore", "msf.utils.js") %>
<% ui.includeJavascript("msfcore", "jquery-dateformat.js") %>
<% ui.includeJavascript("msfcore", "angular.pagination.js") %>
<% ui.includeJavascript("msfcore", "auditLog.controller.js") %>
<% ui.includeJavascript("msfcore", "auditLogsManager.js") %>
<% ui.includeCss("msfcore", "results.css") %>

<h2 style="background-color:#f3f3f3;cursor:pointer;" onclick="toggleFiltersDisplay()">${ui.message("msfcore.filters")}<i id="filters-icon" class="icon-angle-down right"></i></h2>

<div ng-app="auditLogsApp" ng-controller="AuditLogsController" ng-init="retrieveAuditLogs(true)">
<div id="filters">
	<form method="post" id="filter-form" onsubmit="return true">
		${ ui.includeFragment("uicommons", "field/datetimepicker", [id: 'start-time', label: 'msfcore.starttime', formFieldName: 'startTime', useTime: true ]) }
		
		${ ui.includeFragment("uicommons", "field/datetimepicker", [id: 'end-time', label: 'msfcore.endtime', formFieldName: 'endTime', useTime: true ]) }
		
		<br /><br />${ui.message("msfcore.events")}<br />
		<select id="events" multiple="true">
	        <option ng-repeat="event in audits.events" value="{{event}}">{{event}}</option>
	    </select>
	    
	    <br />${ui.message("msfcore.user")}<br />
	    <input type="text" id="viewer" class="field-display ui-autocomplete-input" value="{{audits.user}}">
	    
	    <br />${ui.message("msfcore.patient")} <b id="patient-display">{{audits.patientDisplay}}</b><br />
	    <input type="hidden" name="patientId" id="patient-id" value="{{audits.patientId}}"/>
	    ${ ui.includeFragment("coreapps", "patientsearch/patientSearchWidget", [ showLastViewedPatients: false ]) }
		<br/>
		<input type="button" ng-click="filterAuditLogs()" value="${ui.message('msfcore.filter')}"/>
		<input type="button" onclick="window.location.href='auditLogManager.page'" value="${ ui.message('msfcore.reset')}"/>
	</form>
</div>

<h2 style="background-color:#f3f3f3;cursor:pointer;">${ui.message("msfcore.logsDisplay")}</h2>

<table>
    <thead>
	    <tr>
	        <th>${ ui.message("msfcore.event")}</th>
	        <th>${ ui.message("msfcore.time")}</th>
	        <th>${ ui.message("msfcore.user")}</th>
	        <th>${ ui.message("msfcore.detail")}</th>
	    </tr>
    </thead>
	    <tbody>
	    	<tr ng-repeat="log in audits.auditLogs">
	    		<td>{{log.event}}</td>
	    		<td title="{{log.date}}">{{prettyDate(log.date)}}</td>
	    		<td>{{log.user.display}}</td>
	    		<td>{{log.detail}}</td>
	    	</tr>
	    </tbody>
</table>
<div class="print-ignore wrap-center pagination">
	<div class="left">
		<span><input type="button" onclick="history.back();" value="${ ui.message('general.back')}"/></span>
		<span>${ui.message('msfcore.show')}</span>
		<span>
			<select ng-model="resultsPerPage" ng-change="pagination(this, retrieveAuditLogsInitialisePages)">
				<option value="25">25</option>
				<option value="50" ng-show="audits.pagination.totalItemsNumber > 25">50</option>
				<option value="100" ng-show="audits.pagination.totalItemsNumber > 50">100</option>
				<option value="200" ng-show="audits.pagination.totalItemsNumber > 100">200</option>
				<option value="400" ng-show="audits.pagination.totalItemsNumber > 200">400</option>
				<option value="all" ng-show="audits.pagination.totalItemsNumber > 400">${ui.message('msfcore.all')}</option>
			</select>
		</span>
		<span>${ui.message('msfcore.entries')}</span>
	</div>
	<div class="right showing-pages" ng-init="scp = this">
		<span class='page' ng-repeat="page in pages" ng-class="{'current-page':page.page==currentPage}" ng-click="paginate(scp, page, retrieveAuditLogs)"> {{page.page}} </span>
		<span ng-class="{'page':nextPage, 'disabled': !nextPage}" ng-click="paginate(scp, nextPage, retrieveAuditLogs)">${ui.message('general.next')}</span>
		<span ng-class="{'page':previousPage, 'disabled': !previousPage}" ng-click="paginate(scp, previousPage, retrieveAuditLogs)">${ui.message('general.previous')}</span>
	</div>
	<div class="center pages">
		<span class="disabled">
			${ui.message('msfcore.showing')} {{audits.pagination.fromItemNumber}} ${ui.message('general.to')} {{audits.pagination.toItemNumber}} ${ui.message('general.of')} {{audits.pagination.totalItemsNumber}} ${ui.message('msfcore.entries').toLowerCase()}
		</span>	
	</div>
</div>
</div>