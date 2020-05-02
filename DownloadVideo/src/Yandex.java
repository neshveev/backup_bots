public class Yandex {
    private static final Yandex yandex = new Yandex();

    private Yandex() {
    }

    public static String getDownloadUrl(String url, String fileName) {
        if (url.startsWith("https://yadi.sk/d/")) {
            url = url + "/" + fileName;
        }
        Logger.add("Yandex link is", "https://getfile.dokpub.com/yandex/get/" + url);
        return "https://getfile.dokpub.com/yandex/get/" + url;
    }
}
