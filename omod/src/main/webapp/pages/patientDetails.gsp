<table style="color:white">
    <tr style="border:none">
        <td bgcolor="#424c55" valign="top">
            <b>${ui.message("msfcore.patientdetail.birthdate")}</b>: ${ ui.formatDatePretty(patient.birthdate) } <br />
            <b>${ui.message("msfcore.patientdetail.nationality")}</b>: ${ nationality?nationality:"" } <br />
            <b>${ui.message("msfcore.patientdetail.provenance")}</b>: ${ provenance?provenance:"" } <br />
            <b>${ui.message("msfcore.patientdetail.relationships")}</b>:
            <% if (father) {
                    print "Father: " + ui.message("msfcore.patientdetail.father") + "<br />"
                } %>
            <% if (mother) {
                    print "Mother: " + ui.message("msfcore.patientdetail.mother") + "<br />"
                } %>
            <% if (brother) {
                    print "Brother: " + ui.message("msfcore.patientdetail.brother") + "<br />"
                } %>
            <% if (sister) {
                    print "Sister: " + ui.message("msfcore.patientdetail.sister") + "<br />"
                } %>
            <% if (uncle) {
                    print "Uncle: " + ui.message("msfcore.patientdetail.uncle") + "<br />"
                } %>
            <% if (aunt) {
                    print "Aunt: " + ui.message("msfcore.patientdetail.aunt") + "<br />"
                } %>
            <% if (other) {
                    print "Other: " + ui.message("msfcore.patientdetail.other") + "<br />"
                } %>
        </td>
        <td bgcolor="#424c55" valign="top">
            <b>${ui.message("msfcore.patientdetail.employment")}</b>: ${ employment_status?employment_status:"" }<br />
            <b>${ui.message("msfcore.patientdetail.maritalstatus")}</b>: ${ marital_status?marital_status:"" }<br />
            <b>${ui.message("msfcore.patientdetail.education")}</b>: ${ education?education:"" }<br />
            <b>${ui.message("msfcore.patientdetail.conditionofliving")}</b>: ${ condition_of_living?condition_of_living:"" }<br />
            <b>${ui.message("msfcore.patientdetail.location")}</b>: ${ location?location:"" }<br />
            <b>${ui.message("msfcore.patientdetail.doa")}</b>: ${ ui.formatDatePretty(patient.dateCreated) }<br />
        </td>
        <td bgcolor="#424c55" valign="top">
            <b>${ui.message("msfcore.patientdetail.passportno")}</b>: ${ passport_number?passport_number:"" }<br />
            <b>${ui.message("msfcore.patientdetail.driverslicense")}</b>: ${ drivers_license?drivers_license:"" }<br />
            <b>${ui.message("msfcore.patientdetail.nationalid")}</b>: ${ national_id?national_id:"" }<br />
            <b>${ui.message("msfcore.patientdetail.insurancecard")}</b>: ${ insurance_card?insurance_card:"" }<br />
            <b>${ui.message("msfcore.patientdetail.unhcrid")}</b>: ${ unhchr_id?unhchr_id:"" }<br />
            <b>${ui.message("msfcore.patientdetail.unrwaid")}</b>: ${ unrwa_id?unrwa_id:"" }<br />
        </td>
        <td bgcolor="#424c55" valign="top">
            <b>${ui.message("msfcore.patientdetail.contact")}</b>: ${ telephone?telephone:"" } <br />
            <b>${ui.message("msfcore.patientdetail.address")}</b>: ${ ui.format(patient.personAddress).replace("\n", " ") }<br />
        </td>
    </tr>
</table>