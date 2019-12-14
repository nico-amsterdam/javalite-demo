<#ftl output_format="HTML">

<@content for="title">Contests List</@content>


<div class="message"><@flash name="message"/></div>

<@link_to controller="home">Back to the main menu</@link_to>
<p>

<@link_to action="new_form">Add new contest</@link_to>

<table>
    <tr>
        <td>Name</td>
        <td>Number of prizes</td>
        <td>Edit</td>
    </tr>
<#list contests as contest>
    <tr>
        <td>
            <@link_to action="edit_form" id=contest.id>${contest.name}</@link_to>
        </td>
        <td>
            ${contest.number_of_prizes}
        </td>
        <td>
            <@form  id=contest.id action="delete" method="delete" html_id=contest.id >
                <button type="submit">delete</button>
            </@form>
        </td>
    </tr>
</#list>
</table>




