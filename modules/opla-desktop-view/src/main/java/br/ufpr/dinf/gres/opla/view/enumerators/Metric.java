package br.ufpr.dinf.gres.opla.view.enumerators;

import org.apache.log4j.Logger;

import br.ufpr.dinf.gres.opla.entity.metric.ConventionalMetric;
import br.ufpr.dinf.gres.opla.entity.metric.EleganceMetric;
import br.ufpr.dinf.gres.opla.entity.metric.FeatureDrivenMetric;
import br.ufpr.dinf.gres.opla.entity.metric.PLAExtensibilityMetric;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.AbstractMetricTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.ConventionalMetricTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.EleganceMetricTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.FeatureMetricTableModel;
import br.ufpr.dinf.gres.opla.view.model.tablemodel.PlaExtensibleMetricTableModel;
import br.ufpr.dinf.gres.persistence.dao.ConventionalMetricDAO;
import br.ufpr.dinf.gres.persistence.dao.EleganceMetricDAO;
import br.ufpr.dinf.gres.persistence.dao.FeatureDrivenMetricDAO;
import br.ufpr.dinf.gres.persistence.dao.PLAExtensibilityMetricDAO;
import br.ufpr.dinf.gres.persistence.util.GenericMetricDAO;

/**
 * 
 * @author Fernando
 *
 */
public enum Metric {

	CONVENTIONAL {
		@Override
		@SuppressWarnings("unchecked")
		public GenericMetricDAO<ConventionalMetric> getDAO() {
			return new ConventionalMetricDAO();
		}

		@Override
		public ConventionalMetricTableModel getTableModel() {
			return new ConventionalMetricTableModel();
		}
	},
	FEATUREDRIVEN {
		@Override
		@SuppressWarnings("unchecked")
		public GenericMetricDAO<FeatureDrivenMetric> getDAO() {
			return new FeatureDrivenMetricDAO();
		}

		@Override
		public FeatureMetricTableModel getTableModel() {
			return new FeatureMetricTableModel();
		}
	},
	PLA_EXTENSIBILITY {
		@Override
		@SuppressWarnings("unchecked")
		public GenericMetricDAO<PLAExtensibilityMetric> getDAO() {
			return new PLAExtensibilityMetricDAO();
		}

		@Override
		public AbstractMetricTableModel getTableModel() {
			return new PlaExtensibleMetricTableModel();
		}
	},
	ELEGANCE {
		@Override
		@SuppressWarnings("unchecked")
		public GenericMetricDAO<EleganceMetric> getDAO() {
			return new EleganceMetricDAO();
		}

		@Override
		public EleganceMetricTableModel getTableModel() {
			return new EleganceMetricTableModel();
		}
	};

	private static final Logger LOGGER = Logger.getLogger(Metric.class);

	public abstract <T> GenericMetricDAO<T> getDAO();

	public abstract AbstractMetricTableModel getTableModel();

	public static Metric getMetricByName(String name) {
		try {
			return Metric.valueOf(name.toUpperCase());
		} catch (Exception ex) {
			LOGGER.warn("Metrica não mapeada: " + ex);
			return null;
		}
	}

}
