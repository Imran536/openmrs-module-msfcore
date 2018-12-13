<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("msfcore.auditlogs.manager.label") ])
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}", link: '/' + OPENMRS_CONTEXT_PATH + '/coreapps/systemadministration/systemAdministration.page'},
        { label: "${ ui.message("msfcore.configurations")}"}
    ];
    jQuery(function() {
    	jQuery('#location-codes').slideToggle("slow");
    });
</script>

<div class="note-container">
	<div class="note warning">
		${ui.message("msfcore.configurations.info")}
	</div>
</div>

<div class="content-box all-rounded">
	<form method="post" class="flex-col">
		<h2>${ui.message("msfcore.mandatory")}</h2>

		<div class="input-group flex-col">
			<label>${ui.message("msfcore.instanceId")}</label>
			<input type="text" name="instanceId" value="${instanceId}"/>
		</div>

		<div class="input-group flex-col">
			<label>${ui.message("msfcore.defaultLocation")}</label>
			<select name="defaultLocationUuid">
				<% allLocations.each { loc -> %>
						<option value="${loc.uuid}" <% if(defaultLocation.uuid.equals(loc.uuid)){ %>selected<% } %>>${loc.name}</option>
				<% } %>
			</select>
		</div>

		<div class="input-group flex-col">
			<label>${ui.message("msfcore.localFeedUrl")}</label>
			<input type="text" name="localFeedUrl" value="${localFeedUrl}"/>	
		</div>

		<% if(isClinic){ %>
			<div class="input-group flex-col">
				<label>${ui.message("msfcore.parentFeedUrl")}</label>
				<input type="text" name="parentFeedUrl" value="${parentFeedUrl}"/>
			</div>
		<% } %>

		<div class="flex-row">
			<h2>${ui.message("msfcore.locationConfigs")}</h2>
			<button onclick="jQuery('#location-codes').toggle();">Toggle Locations</button>
		</div>

		<div id="location-codes">
			<table>
				<thead>
					<tr>
						<th>${ ui.message("Location.title")}</th>
						<th>${ ui.message("msfcore.code")}</th>
						<th>${ ui.message("msfcore.uid")}</th>
					</tr>
				</thead>
				<% msfLocations.each { mLoc -> %>
					<tbody>
						<tr>
							<td>${mLoc.display}</td>
							<td><input type="text" name="${mLoc.uuid}" value="${mLoc.code}" /></td>
							<td><input type="text" name="${mLoc.uuid}_uid" value="${mLoc.uid}" /></td>
						</tr>
					</tbody>
				<% } %>
			</table>
		</div>
		<input type="submit" value="${ ui.message('general.submit')}"/>
	</form>
</div>
