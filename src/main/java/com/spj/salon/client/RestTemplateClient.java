package com.spj.salon.client;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Yogesh Sharma
 *
 */
@Service
public class RestTemplateClient{

	//@Autowired
	//private RestTemplate restTemplate;
	
	/**
	 * 
	 * @param address
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	/*
	 * public Map<String, Double> getLongitideLatitude(Address address) throws
	 * UnsupportedEncodingException { Map<String, Double> longiLatiMap = new
	 * HashMap<>();
	 * 
	 * HttpHeaders headers = new HttpHeaders(); headers.set("Accept",
	 * MediaType.ALL_VALUE);
	 * 
	 * UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("")
	 * .queryParam("access_key", "") .queryParam("query",
	 * URLEncoder.encode(address.getAddress(), "UTF-8"));
	 * 
	 * HttpEntity<?> entity = new HttpEntity<>(headers);
	 * 
	 * ResponseEntity<Root> positionStackRootEntity = restTemplate.exchange(
	 * builder.toUriString(), HttpMethod.GET, entity, Root.class);
	 * 
	 * if(positionStackRootEntity.getBody()!=null &&
	 * positionStackRootEntity.getBody().getData()!=null &&
	 * !CollectionUtils.isEmpty(positionStackRootEntity.getBody().getData())) {
	 * 
	 * List<Data> positionStackData = positionStackRootEntity.getBody().getData();
	 * longiLatiMap.put("longitude", positionStackData.get(0).getLongitude());
	 * longiLatiMap.put("latitude", positionStackData.get(0).getLatitude()); }
	 * return longiLatiMap; }
	 */
}
