import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class QuizLeaderboard {

    public static void main(String[] args) throws Exception {

        String regNo = "RA2311003010666"; 

        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int i = 0; i < 10; i++) {

            String urlStr = "https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages?regNo="
                    + regNo + "&poll=" + i;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONArray events = json.getJSONArray("events");

            for (int j = 0; j < events.length(); j++) {
                JSONObject event = events.getJSONObject(j);

                String roundId = event.getString("roundId");
                String participant = event.getString("participant");
                int score = event.getInt("score");

                String key = roundId + "_" + participant;

                if (!seen.contains(key)) {
                    seen.add(key);
                    scores.put(participant,
                            scores.getOrDefault(participant, 0) + score);
                }
            }

            Thread.sleep(5000); // ⏱ Mandatory delay
        }

        // Sort leaderboard
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(scores.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        JSONArray leaderboard = new JSONArray();
        int total = 0;

        for (Map.Entry<String, Integer> entry : list) {
            JSONObject obj = new JSONObject();
            obj.put("participant", entry.getKey());
            obj.put("totalScore", entry.getValue());

            leaderboard.put(obj);
            total += entry.getValue();
        }

   
        URL url = new URL("https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/submit");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject request = new JSONObject();
        request.put("regNo", regNo);
        request.put("leaderboard", leaderboard);

        OutputStream os = conn.getOutputStream();
        os.write(request.toString().getBytes());
        os.flush();
        os.close();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
    }
}
