package com.o2o.action.server.app;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.actions.api.response.helperintent.SelectionList;
import com.google.api.services.actions_fulfillment.v2.model.*;
import com.google.gson.Gson;
import com.o2o.action.server.DTO.*;
import com.o2o.action.server.util.CognitiveService;
import com.o2o.action.server.util.CommonUtil;
import com.o2o.action.server.util.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class internApp extends DialogflowApp {

    RetrofitClient retrofitClient = new RetrofitClient();
    private String analyticsUrl;
    private String analyticsContext = "";

    @ForIntent("Default Welcome Intent")
    public ActionResponse defaultWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();

        data.clear();

        List<String> suggestions = new ArrayList<String>();
        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();
        rb.addSuggestions(new String[]{"네이버", "네이버 뉴스"});

        simpleResponse.setTextToSpeech("안녕하세요, 기사 봇 입니다. 원하시는 매체를 선택해주세요!")
                .setDisplayText("안녕하세요, 기사 봇 입니다.\n 원하시는 매체를 선택해주세요!")
        ;
        basicCard
                .setTitle("기사 봇입니다. 원하시는 매체를 선택해주세요~")
                .setFormattedText("네이버를 말해주세요!^^")
                .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr3/navernews.png")

                        .setAccessibilityText("home"))
                .setImageDisplayOptions("WHITE")
        ;
        rb.add(simpleResponse);
        rb.add(basicCard);

        rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));
        return rb.build();
    }

    @ForIntent("SearchMethodSelect")
    public ActionResponse searchMethodSelect(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();

        data.clear();

        List<String> suggestions = new ArrayList<String>();
        SimpleResponse simpleResponse = new SimpleResponse();

        simpleResponse.setTextToSpeech("검색 수단을 선택해주세요!")
                .setDisplayText("검색 수단을 선택해주세요!")
        ;

        rb.add(simpleResponse);
        rb.addSuggestions(new String[]{"실시간 검색어", "직접", "카테고리"});
        rb.add(
                new CarouselBrowse()
                        .setItems(
                                new ArrayList<CarouselBrowseItem>(
                                        Arrays.asList(
                                                new CarouselBrowseItem()
                                                        .setTitle("실시간 검색어 검색")
                                                        .setDescription("실시간 검색어 검색")
                                                        .setOpenUrlAction(new OpenUrlAction().setUrl("https://example.com"))
                                                        .setImage(
                                                                new Image()
                                                                        .setUrl(
                                                                                "https://actions.o2o.kr/devsvr3/realtime.png")
                                                                        .setAccessibilityText("실시간 검색"))
                                                        ,
                                                new CarouselBrowseItem()
                                                        .setTitle("자유검색")
                                                        .setDescription("자유 검색")
                                                        .setOpenUrlAction(new OpenUrlAction().setUrl("https://actions.o2o.kr/devsvr3/navernews.png"))
                                                        .setImage(
                                                                new Image()
                                                                        .setUrl(
                                                                                "https://actions.o2o.kr/devsvr3/freesearch.png")
                                                                        .setAccessibilityText("자유검색"))
                                                        ,
                                                new CarouselBrowseItem()
                                                        .setTitle("카테고리 검색")
                                                        .setDescription("카테고리 검색")
                                                        .setOpenUrlAction(new OpenUrlAction().setUrl("https://example.com"))
                                                        .setImage(
                                                                new Image()
                                                                        .setUrl(
                                                                                "https://actions.o2o.kr/devsvr3/category.png")
                                                                        .setAccessibilityText("카테고리 검색"))
                                                        ))));


        return rb.build();
    }

    @ForIntent("SearchArticles")
    public ActionResponse categorySearchArticles(ActionRequest request) throws ExecutionException, InterruptedException, IOException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        List<String> suggestions = new ArrayList<String>();

        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();
        SelectionList selectionList = new SelectionList();
        List<ListSelectListItem> listSelectListItems = new ArrayList<>();
        ListSelectListItem item = new ListSelectListItem();

        ArrayList<CategoryDTO> categoryDTOResult;
        ArrayList<RealtimeWordDTO> realtimeWordDTOResult;

        data.clear();
        String isParameter = "";
        String parameter = CommonUtil.makeSafeString(request.getParameter("searchmethod"));
        String fullRequest = request.getRawText();
        String result = "";

        simpleResponse.setTextToSpeech(parameter + " 검색 수단을 선택하셨습니다.")
                .setDisplayText(parameter + " 검색 수단을 선택하셨습니다.")
        ;

        if (parameter.equals("카테고리")) {
            //카테고리 통신
            Call<ArrayList<CategoryDTO>> call = retrofitClient.apiService.selectCategory();
            categoryDTOResult = call.execute().body();

            for (CategoryDTO dto : categoryDTOResult) {
                System.out.println("dto: " + dto.getName());
                item = new ListSelectListItem();
                item.setTitle(dto.getName())
                        .setDescription(
                                dto.getName() + " 카테고리 기사를 보실 수 있습니다.")
                        .setImage(
                                new Image()
                                        .setUrl(
                                                "https://actions.o2o.kr/devsvr3/" + dto.getName() + ".png")
                                        .setAccessibilityText(dto.getName()))
                        .setOptionInfo(
                                new OptionInfo()
                                        .setSynonyms(
                                                Arrays.asList(
                                                        "Google Home Assistant",
                                                        "Assistant on the Google Home"))
                                        .setKey(dto.getName()));
                listSelectListItems.add(item);
            }//for

            selectionList.setItems(listSelectListItems);
            rb.add(selectionList);

        } else if (parameter.equals("자유")) {

            basicCard
                    .setTitle("자유 검색을 선택하셨습니다.")
                    .setFormattedText("원하시는 검색어를 말해주세요!")
                    .setImage(new Image().setUrl("https://actions.o2o.kr/devsvr3/freesearch.png")
                            .setAccessibilityText("home"));

        } else if (parameter.equals("실시간 검색어")) {
            Call<String> callForInsertRealtimeWord = retrofitClient.apiService.insertRealtimeWord();
            result = callForInsertRealtimeWord.execute().body();

            System.out.println(result);

            Call<ArrayList<RealtimeWordDTO>> call = retrofitClient.apiService.selectRealtimeWord();
            realtimeWordDTOResult = call.execute().body();

            for (RealtimeWordDTO dto : realtimeWordDTOResult) {
                System.out.println("dto: " + dto.getWord());
                item = new ListSelectListItem();
                item.setTitle(dto.getWord())
                        .setDescription(
                                dto.getWord() + " 실시간 검색어 검색 기사를 보실 수 있습니다.")
                        .setImage(
                                new Image()
                                        .setUrl(
                                                "https://actions.o2o.kr/devsvr3/realtime.png")
                                        .setAccessibilityText(dto.getWord()))
                        .setOptionInfo(
                                new OptionInfo()
                                        .setSynonyms(
                                                Arrays.asList(
                                                        "Google Home Assistant",
                                                        "Assistant on the Google Home"))
                                        .setKey(dto.getWord()));
                listSelectListItems.add(item);
            }//for

            selectionList.setItems(listSelectListItems);
            rb.add(selectionList);
        }


        rb.add(simpleResponse);
        rb.add(basicCard);
        rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));
        return rb.build();
    }

    @ForIntent("ArticleList")
    public ActionResponse articleList(ActionRequest request) throws ExecutionException, InterruptedException, IOException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        List<String> suggestions = new ArrayList<String>();

        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();

        SelectionList selectionList = new SelectionList();
        List<ListSelectListItem> listSelectListItems = new ArrayList<>();
        ListSelectListItem item = new ListSelectListItem();

        ArrayList<CategoryNewsDTO> categoryDTONewsResult;
        ArrayList<NewsDTO> newsDTOResult;

        data.clear();
        String category_parameter = CommonUtil.makeSafeString(request.getParameter("category"));
        String any_parameter = CommonUtil.makeSafeString(request.getParameter("any"));

        String fullRequest = request.getRawText();
        String result = "";
        System.out.println("category_parameter: " + category_parameter);
        if (!category_parameter.isEmpty()) {
            Call<ArrayList<CategoryNewsDTO>> call = retrofitClient.apiService.selectOneCategoryNews(category_parameter);
            categoryDTONewsResult = call.execute().body();

            for (CategoryNewsDTO dto : categoryDTONewsResult) {
                System.out.println("dto: " + dto.getTitle());
                item = new ListSelectListItem();
                item.setTitle(dto.getTitle())
                        .setDescription(
                                category_parameter + " 카테고리 기사")
                        .setImage(
                                new Image()
                                        .setUrl(
                                                dto.getUrl())
                                        .setAccessibilityText(dto.getAid()))
                        .setOptionInfo(
                                new OptionInfo()
                                        .setSynonyms(
                                                Arrays.asList(
                                                        "Google Home Assistant",
                                                        "Assistant on the Google Home"))
                                        .setKey(dto.getAid()));
                listSelectListItems.add(item);
            }//for

            selectionList.setItems(listSelectListItems);
            rb.add(selectionList);

            simpleResponse.setTextToSpeech(category_parameter + " 카테고리를 선택하셨습니다.")
                    .setDisplayText(category_parameter + " 카테고리를 선택하셨습니다.")
            ;
        }//if
        else if (!any_parameter.isEmpty()) {
            Call<String> callForInsertSearchNews = retrofitClient.apiService.insertSearchNews(any_parameter);
            result = callForInsertSearchNews.execute().body();

            System.out.println(result);

            Call<ArrayList<NewsDTO>> call = retrofitClient.apiService.selectOneSearchNews(any_parameter);
            newsDTOResult = call.execute().body();

            if (newsDTOResult != null) {
                for (NewsDTO dto : newsDTOResult) {
                    System.out.println("dto: " + dto.getTitle());
                    item = new ListSelectListItem();
                    item.setTitle(dto.getTitle())
                            .setDescription(
                                    dto.getContext())
                            .setImage(
                                    new Image()
                                            .setUrl(
                                                    dto.getImgurl())
                                            .setAccessibilityText(dto.getAid()))
                            .setOptionInfo(
                                    new OptionInfo()
                                            .setKey(dto.getAid()));
                    listSelectListItems.add(item);
                }//for
            }

            selectionList.setItems(listSelectListItems);
            rb.add(selectionList);

            simpleResponse.setTextToSpeech(any_parameter + " 검색어로 검색합니다")
                    .setDisplayText(any_parameter + " 검색어로 검색합니다")
            ;
        }//else if

        rb.add(simpleResponse);
        rb.add(basicCard);
        rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));
        return rb.build();
    }

    @ForIntent("SelectListItem")
    public ActionResponse listSelected(ActionRequest request) throws IOException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        String selectedItem = request.getSelectedOption();
        NewsDTO newsDTOResult;

        data.clear();

        List<String> suggestions = new ArrayList<String>();
        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();
        rb.addSuggestions(new String[]{"처음으로", "긍정부정 분석"});

        simpleResponse.setTextToSpeech("해당 기사를 검색하였습니다. ")
                .setDisplayText("해당 기사를 검색하였습니다.")
        ;

        Call<NewsDTO> call = retrofitClient.apiService.selectDetailNews(selectedItem);
        newsDTOResult = call.execute().body();

        System.out.println("newsDTOResult: " + newsDTOResult.getAid());
        analyticsUrl = newsDTOResult.getUrl();

        Call<NewsDTO> call2 = retrofitClient.apiService.updateNews(analyticsUrl);
        call2.enqueue(new Callback<NewsDTO>() {
            @Async
            @Override
            public void onResponse(@NotNull Call<NewsDTO> call2, Response<NewsDTO> response) {
                if (response.isSuccessful()) {
                    analyticsContext = "";
                    NewsDTO newsDTOResult = response.body();
                    System.out.println("newsDTOResult.getContext(): " + newsDTOResult.getFullcontext());

                    //텍스트 정제
                    String value = newsDTOResult.getFullcontext();

                    value = value.replaceAll("[a-zA-Z]", "");
                    value = value.replaceAll("[0-9]", "");
                    value = value.replaceAll("이미지 원본보기", "");
                    value = value.replaceAll("무단전재&재배포 금지", "");
                    value = value.replaceAll("무단전재 및 재배포 금지", "");
                    value = value.replaceAll("무단 전재 및 재배포 금지", "");
                    value = value.replaceAll("[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>▶▽♡◀━@\\#$%&\\\\\\=\\(\\'\\\"ⓒ©(\\n)(\\t)]", "");

                    analyticsContext = value;
                    System.out.println("Asynchronous Success");
                } else {
                    System.out.println(response.toString());
                }
            }

            @Override
            public void onFailure(Call<NewsDTO> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Asynchronous failed");

            }
        });


        basicCard
                .setTitle(newsDTOResult.getTitle())
                .setFormattedText(newsDTOResult.getContext())
                .setImage(new Image().setUrl(newsDTOResult.getImgurl())
                        .setAccessibilityText(newsDTOResult.getWord()))
                .setImageDisplayOptions("CROPPED")
                .setButtons(
                        new ArrayList<Button>(
                                Arrays.asList(
                                        new Button()
                                                .setTitle("기사 원문 보기")
                                                .setOpenUrlAction(
                                                        new OpenUrlAction().setUrl(newsDTOResult.getUrl())))));


        rb.add(simpleResponse);
        rb.add(basicCard);

        rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));
        return rb.build();
    }


    @ForIntent("ArticleAnalytics")
    public ActionResponse articleAnalytics(ActionRequest request) throws Exception {

        CognitiveService cognitiveService = new CognitiveService();
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        NewsDTO newsDTOResult;

        data.clear();

        List<String> suggestions = new ArrayList<String>();
        suggestions.add("처음으로");

        SimpleResponse simpleResponse = new SimpleResponse();
        BasicCard basicCard = new BasicCard();
        TableCard tableCard = new TableCard();

        simpleResponse.setTextToSpeech("긍정 부정 분석 결과입니다.")
                .setDisplayText("긍정 부정 분석 결과입니다.")
        ;

        if (analyticsContext != "") {

//            Call<NewsDTO> call = retrofitClient.apiService.updateNews(analyticsUrl);
//            newsDTOResult = call.execute().body();
//            System.out.println("newsDTOResult.getContext(): " + newsDTOResult.getContext());
//
//            //텍스트 정제
//            String value = newsDTOResult.getContext();
//
//            value = value.replaceAll("[a-zA-Z]", "");
//            value = value.replaceAll("[0-9]", "");
//            value = value.replaceAll("이미지 원본보기", "");
//            value = value.replaceAll("무단전재&재배포 금지", "");
//            value = value.replaceAll("무단전재 및 재배포 금지", "");
//            value = value.replaceAll("무단 전재 및 재배포 금지", "");
//            value = value.replaceAll("[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>▶▽♡◀━@\\#$%&\\\\\\=\\(\\'\\\"ⓒ©(\\n)(\\t)]","");
//

            String analyticsResult = cognitiveService.getTheSentimentPrettify(analyticsContext);
            System.out.println("analyticsResult: " + analyticsResult);

            //Gson
            Gson gson = new Gson();
            ResultDTO result = new ResultDTO();
            result = gson.fromJson(analyticsResult, ResultDTO.class);

            System.out.println("positive: " + result.getDocuments().get(0).getConfidenceScores().getPositive());
            System.out.println("negative: " + result.getDocuments().get(0).getConfidenceScores().getNegative());
            System.out.println("neutral: " + result.getDocuments().get(0).getConfidenceScores().getNeutral());

            rb
                    .add("긍정부정 분석 결과 입니다.")
                    .add(
                            new TableCard()
                                    .setColumnProperties(
                                            Arrays.asList(
                                                    new TableCardColumnProperties().setHeader("긍정 요소"),
                                                    new TableCardColumnProperties().setHeader("부정 요소"),
                                                    new TableCardColumnProperties().setHeader("중립 요소")))
                                    .setRows(
                                            Arrays.asList(
                                                    new TableCardRow()
                                                            .setCells(
                                                                    Arrays.asList(
                                                                            new TableCardCell().setText(String.format("%.2f",100 * result.getDocuments().get(0).getConfidenceScores().getPositive())+ "%"),
                                                                            new TableCardCell().setText(String.format("%.2f",100 * result.getDocuments().get(0).getConfidenceScores().getNegative()) + "%"),
                                                                            new TableCardCell().setText(String.format("%.2f",100 * result.getDocuments().get(0).getConfidenceScores().getNeutral()) + "%")))
                                            )));
//            tableCard
//                    .setTitle("긍정/부정 요소 분석 결과입니다")
//                    .setSubtitle("")
//                    .setColumnProperties(
//                            Arrays.asList(
//                                    new TableCardColumnProperties().setHeader("긍정 요소"),
//                                    new TableCardColumnProperties().setHeader("부정 요소"),
//                                    new TableCardColumnProperties().setHeader("중립 요소")))
//                    .setRows(
//                            Arrays.asList(
//                                    new TableCardRow()
//                                            .setCells(
//                                                    Arrays.asList(
//                                                            new TableCardCell().setText(100*result.getDocuments().get(0).getConfidenceScores().getPositive()+"%"),
//                                                            new TableCardCell().setText(100*result.getDocuments().get(0).getConfidenceScores().getNegative()+"%"),
//                                                            new TableCardCell().setText(100*result.getDocuments().get(0).getConfidenceScores().getNeutral()+"%")
//                                                    ))));
//            rb.add(tableCard);

        }
        rb.add(simpleResponse);
        //rb.add(basicCard);

        rb.addSuggestions(suggestions.toArray(new String[suggestions.size()]));

        return rb.build();
    }
}

