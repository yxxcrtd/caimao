package genertor;

import java.io.File;

import velocity.ContentEngine;

public class VelocityMain {
	

	public static void main(String[] args) {

	}

	public  void genertor(final String modelName, final String pk) {
		// TODO Auto-generated method stub
		String projectBasePath = new File("").getAbsolutePath();

		String mapperBasePath = projectBasePath
				+ "/src/main/java/com/caimao/bana/server/mapper/";

		File mapperFile = new File(mapperBasePath);
		if (!mapperFile.exists())
			new File(mapperBasePath).mkdirs();
		if (!filerFile(mapperBasePath + modelName + "Mapper")) {

			String[] args = new String[] { "3",
					projectBasePath + "/src/entityexe/resources/velocity/",
					"Object.properties", "ObjectMapper.vm", modelName,
					mapperBasePath, pk };

			new ContentEngine().generatorVelocity(args);

		
		}

		System.out.println("generator=" + modelName + "mapper success");

		String servicesBasePath = "E:/git/caimao/bana/bana-server/bana-api/src/main/java"
				+ "/com/caimao/bana/api/service/";

		File servicesFile = new File(servicesBasePath);
		if (!servicesFile.exists())
			new File(servicesBasePath).mkdirs();
		if (!filerFile(servicesBasePath + modelName + "Services")) {

			String[] args = new String[] { "1",
					projectBasePath + "/src/entityexe/resources/velocity/",
					"Object.properties", "ObjectServices.vm", modelName,
					servicesBasePath, pk };

			new ContentEngine().generatorVelocity(args);

			
		}

		System.out.println("generator=" + modelName + "servcice success");

	}

	private  boolean filerFile(String fileName) {
		return fileName.contains("CompressedMapper.java");
	}
}
