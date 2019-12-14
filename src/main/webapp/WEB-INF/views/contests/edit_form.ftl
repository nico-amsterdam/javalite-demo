<#ftl output_format="HTML">

<@content for="title">Update contest: ${contest.name}</@content>

<script>
    function appendParticipantRow(_elm, data) {
        $("#participant_rows").append(data);
    }
</script>


<@link_to>Back to all contests</@link_to>

<span class="error_message"><@flash name="message"/></span>
<h2>Adding new contest</h2>


<@form action="save" method="post">
    <table style="margin:30px">
        <tr>
            <td>Contest name
            <input type="hidden" name="id" value="${(contest.id)!}">
            <td><input type="text" name="name" value="${(contest.name)!}"> *
                <span class="error">${(errors.name)!}</span>
        
        </tr>
        <tr>
            <td>Number of prizes:
            <td><input type="number" min="0" name="number_of_prizes" value="${(contest.number_of_prizes)!}"> *
                <span class="error">${(errors.number_of_prizes)!}</span>
        </tr>
        <tr>
            <td>prize 1:
            <td><input type="text" name="prize1" value="${(contest.prize1)!}"> *
                <span class="error">${(errors.prize1)!}</span>
        </tr>
    </table>

    <table>
      <thead>
        <tr>
            <th>Name
            <th>E-mail address
            <th>Date of birth
        </tr>'
      </thead>
      <tbody id="participant_rows">
    <#list participants as participant>
        <#assign indexedId    = 'r' + participant?index + '_id'>
        <#assign indexedName  = 'r' + participant?index + '_name'>
        <#assign indexedEmail = 'r' + participant?index + '_email'>
        <tr>
            <td><input type="hidden" name="${indexedId}"    value="${participant.id}">
                <input type="text"   name="${indexedName}"  value="${participant.name}">
            <td><input type="text"   name="${indexedEmail}" value="${participant.email}">
            <td>${participant.date_of_birth!}
        </tr>
    </#list>
      </tbody>
      <tfoot>
        <tr>
            <td collspan="3"><@link_to action="add_participant" after="appendParticipantRow">New participant</@link_to>
        </tr>
       </tfoot>
    </table>

    <table>
        <tr>
            <td><@link_to>Cancel</@link_to> | <input type="submit" value="Save contest">
        </tr>
    </table>

</@form>



