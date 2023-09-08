package global;

import domain.itinerary.service.ItineraryService;

public class Main {
    public static void main(String[] args) {
        ItineraryService itineraryService = new ItineraryService();

        itineraryService.getItineraryListFromJson(1);
//        itineraryService.recordItinerary(1);
    }
}
