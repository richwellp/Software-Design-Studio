package student.adventure;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class AdventureTest {
    private String defaultInput;
    private Adventure defaultAdventure;
    private String defaultMenuOutput;
    ByteArrayInputStream inputStream;
    ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        String[] actions = {"Y", "src/test/resources/siebel.json"};
        defaultInput = "N" + "\n"; // or {"Y", "src/test/resources/siebel.json"}
        inputStream = new ByteArrayInputStream(defaultInput.getBytes());
        defaultAdventure = new Adventure();
        defaultMenuOutput = defaultAdventure.menu(inputStream, outputStream);
    }

    @Test
    public void testDefaultMenu() {
        Assert.assertThat(defaultMenuOutput, CoreMatchers.containsString("Success"));
    }

    @Test
    public void testValidSchema() {
        inputStream = new ByteArrayInputStream(defaultInput.getBytes());
        Adventure adventure1 = new Adventure();
        String menuOutput = adventure1.menu(inputStream, outputStream);

        Assert.assertThat(menuOutput, CoreMatchers.containsString("Success"));
    }

    @Test
    public void testInvalidSchema() {
        String[] actions = {"Y", "src/test/resources/invalidSchema.json"};
        String input = String.join("\n", actions) + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        Adventure adventure1 = new Adventure();
        String menuOutput = adventure1.menu(inputStream, outputStream);

        Assert.assertThat(menuOutput, CoreMatchers.containsString("Invalid"));
    }

    @Test
    public void testInvalidLayout() {
        String[] actions = {"Y", "src/test/resources/invalidSchema.json"};
        String input = String.join("\n", actions) + "\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        Adventure adventure1 = new Adventure();
        String menuOutput = adventure1.menu(inputStream, outputStream);
        Assert.assertThat(menuOutput, CoreMatchers.containsString("Invalid"));
    }

    @Test
    public void testExit() {
        String input = "exit\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);

        Assert.assertEquals(startOutput, "Game Over");
    }

    @Test
    public void testGoSomeDirection() {
        String input = "go east\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);

        Assert.assertThat(startOutput, CoreMatchers.containsString("going "));
    }

    @Test
    public void testCaseCommands() {
        String input = "gO EAsT\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);

        Assert.assertThat(startOutput, CoreMatchers.containsString("going "));
    }

    @Test
    public void testPrintDescription() {
        String input = " ";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);
        String output = outputStream.toString();

        Assert.assertTrue(output.contains("You are on Matthews, outside the Siebel Center"));
        Assert.assertTrue(output.contains("East"));
    }

    @Test
    public void testFinishGame() {
        String input = "Go East\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);
        input = "gO eaST\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        startOutput = defaultAdventure.startAdventure(inputStream);
        input = "Go SoUtH\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        startOutput = defaultAdventure.startAdventure(inputStream);
        String output = outputStream.toString();

        Assert.assertThat(output, CoreMatchers.containsString("You have reached the final room."));
    }

    @Test
    public void testGoInvalidDirection() throws Exception {
        String input = "gO over there\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);

        Assert.assertThat(startOutput, CoreMatchers.containsString("I can't go "));
    }

    @Test
    public void testUnrecognizedOrder() {
        String input = "helpme\n";
        inputStream = new ByteArrayInputStream(input.getBytes());
        String startOutput = defaultAdventure.startAdventure(inputStream);

        Assert.assertThat(startOutput, CoreMatchers.containsString("I don't understand '"));
    }
}