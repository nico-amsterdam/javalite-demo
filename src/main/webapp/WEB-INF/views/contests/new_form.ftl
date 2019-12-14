<@content for="title">Add new contest</@content>

<span class="error_message"><@flash name="message"/></span>
<h2>Adding new contest</h2>


<@form action="create" method="post">
    <table style="margin:30px">
        <tr>
            <td>Contest name</td>
            <td><input type="text" name="name" value="${(flasher.params.name)!}"> *
                            <span class="error">${(flasher.errors.name)!}</span>
            </td>
        </tr>
        <tr>
            <td>Number of prizes:</td>
            <td><input type="number" min="0" name="number_of_prizes" value="${(flasher.params.number_of_prizes)!}"> *
                            <span class="error">${(flasher.errors.number_of_prizes)!}</span>
            </td>
        </tr>
        <tr>
            <td>prize 1:</td>
            <td><input type="text" name="prize1" value="${(flasher.params.prize1)!}"> *
                <span class="error">${(flasher.errors.prize1)!}</span>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><@link_to>Cancel</@link_to> | <input type="submit" value="Add new contest"></td>

        </tr>
    </table>
</@form>



