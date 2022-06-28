package it.polito.tdp.yelp.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private List<String> cities;
	private Graph<Review, DefaultWeightedEdge> grafo;
	
	private List<Review> best;
	private int numGiorni;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public List<String> getCities() {
		if(this.cities==null)
			this.cities = dao.getCities();
		
		return this.cities;
	}
	
	public List<Business> getBusinessByCity(String city) {
		return dao.getBusinessByCity(city);
	}
	
	public String creaGrafo(Business b) {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getReviewsByBusiness(b));
		
		for(Review r1 : grafo.vertexSet()) {
			for(Review r2 : grafo.vertexSet()) {
				if(!r1.equals(r2) && this.grafo.getEdge(r1, r2)==null) {
					if(r1.getDate().isBefore(r2.getDate())) {
						long peso = ChronoUnit.DAYS.between(r1.getDate(), r2.getDate());
						if(peso!=0) {
							Graphs.addEdge(grafo, r1, r2, peso);
						}
					}
				}
			}
		}
		
		return "Grafo creato: "+grafo.vertexSet().size()+" vertici, "+grafo.edgeSet().size()+" archi";
	}
	
	public List<ReviewArchi> getReviewMaxArchiOut() {
		List<ReviewArchi> reviewsMaxArchi = new ArrayList<>();
		int max = 0;
		for(Review r : grafo.vertexSet()) {
			int count = grafo.outgoingEdgesOf(r).size();
			
			if(count > max) {
				max = count;
			}
		}
		
		for(Review r : grafo.vertexSet()) {
			int count = grafo.outgoingEdgesOf(r).size();
			
			if(count==max) {
				reviewsMaxArchi.add(new ReviewArchi(r, count));
			}
		}
		return reviewsMaxArchi;
	}
	
	public List<Review> trovaMiglioramento() {
		if(this.grafo==null)
			return null;
		
		this.best = new ArrayList<>();
		this.numGiorni = 0;
		List<Review> parziale = new ArrayList<>();
		for(Review r : grafo.vertexSet()) {
			parziale.add(r);
			cerca(parziale, 0);
			parziale.clear();
		}
		
		return this.best;
	}

	private void cerca(List<Review> parziale, int numGiorniParziale) {
		if(parziale.size() > best.size()) {
			this.best = new ArrayList<>(parziale);
			this.numGiorni = numGiorniParziale;
		}
		
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(parziale.get(parziale.size()-1))) {
			Review vicina = Graphs.getOppositeVertex(grafo, e, parziale.get(parziale.size()-1));
			if(!parziale.contains(vicina) && vicina.getStars() >= parziale.get(parziale.size()-1).getStars()) {
				parziale.add(vicina);
				numGiorniParziale += grafo.getEdgeWeight(e);
				cerca(parziale, numGiorniParziale);
				parziale.remove(parziale.size()-1);
				numGiorniParziale -= grafo.getEdgeWeight(e);
			}
		}
	}
	
	public int getNumGiorni() {
		return this.numGiorni;
	}
	
}
