package com.dpworld.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.Report;
import com.dpworld.persistence.entity.ReportType;
import com.dpworld.persistence.vo.GenericReportDataResponse;
import com.dpworld.persistence.vo.MismatchReportDataResponse;
import com.dpworld.persistence.vo.ReportListResponse;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

	Report findByReportId(Long reportId);
	
	@Query(value = "SELECT "
			+ "    inbdata.inbDeclarationNumber     inbdeclarationnumber, "
			+ "    CASE WHEN inbdata.createdDate IS NOT NULL THEN inbdata.createdDate ELSE outbdata.createdDate END docdate, "
			+ "    inbdata.hsCode                    inbhscode, "
			+ "    inbdata.barcode                    inbbarcode, "
			+ "    inbdata.itemDescription           inbitemdescription, "
			+ "    inbdata.totalCifValue            inbtotalcifvalue, "
			+ "    outbdata.inbDeclarationNumber    outbinbdeclarationnumber, "
			+ "    outbdata.hsCode                   outbhscode, "
			+ "    outbdata.barcode                   outbbarcode, "
			+ "    outbdata.itemDescription          outbitemdescription, "
			+ "    outbdata.totalCifValue           outbtotalcifvalue , "
			+ "    CASE WHEN inbdata.barcode IS NULL THEN ( CASE WHEN inbdata.cargoPiecesQuantity IS NULL THEN 0 ELSE inbdata.cargoPiecesQuantity END ) ELSE inbdata.barcodePiecesQuantity END inbtotalquantity, "
			+ "    CASE WHEN inbdata.barcode IS NULL THEN ( CASE WHEN inbdata.cargoPiecesNetWeight IS NULL THEN 0 ELSE inbdata.cargoPiecesNetWeight END ) ELSE inbdata.barcodePiecesNetWeight END inbtotalweight, "
			+ "    CASE WHEN outbdata.barcode IS NULL THEN ( CASE WHEN outbdata.cargoPiecesQuantity IS NULL THEN 0 ELSE outbdata.cargoPiecesQuantity END ) ELSE outbdata.barcodePiecesQuantity END outbtotalquantity, "
			+ "    CASE WHEN outbdata.barcode IS NULL THEN ( CASE WHEN outbdata.cargoPiecesNetWeight IS NULL THEN 0 ELSE outbdata.cargoPiecesNetWeight END ) ELSE outbdata.barcodePiecesNetWeight END outbtotalweight "
			+ "        FROM (  "
			+ "				SELECT  "
			+ "    				inboundDetails.inbDeclarationNumber, inboundDetails.createdDate, inboundDetails.hsCode,  "
			+ "    				inboundDetails.itemDescription, inboundDetails.barcode, inboundDetails.barcodePiecesQuantity,  "
			+ "    				inboundDetails.barcodePiecesNetWeight, inboundDetails.cargoPiecesQuantity, cargoPiecesNetWeight,  "
			+ "    				inbCifDetails.totalCifValue FROM ( "
			+ "        				SELECT decl.declaration_number inbDeclarationNumber,   "
			+ "        				to_char(inb.created_time, 'dd-MM-yyyy') createdDate, "
			+ "        				(SELECT hs_code FROM hs_code WHERE hs_code_id = hsc.hs_code_id) hsCode,  "
			+ "        				(SELECT hs_code_decription FROM hs_code WHERE hs_code_id = hsc.hs_code_id) itemDescription, "
			+ "        				(SELECT barcode FROM bar_code WHERE barcode_id = inb_barcode.barcode_id) barcode,   "
			+ "        				SUM(inb_barcode.pieces_quantity) barcodePiecesQuantity,   "
			+ "        				SUM(inb_barcode.pieces_net_weight) barcodePiecesNetWeight,   "
			+ "       				SUM(cargo.pieces_quantity) cargoPiecesQuantity, "
			+ "        				SUM(cargo.pieces_net_weight) cargoPiecesNetWeight "
			+ "        			FROM inbound inb   "
			+ "            			LEFT JOIN inb_declaration_information decl ON inb.inbound_id = decl.inbound_id  "
			+ "            			LEFT JOIN inb_cargo_information cargo ON inb.inbound_id = cargo.inbound_id "
			+ "            			LEFT JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id "
			+ "            			LEFT JOIN inb_barcode_details inb_barcode ON cargo.id = inb_barcode.inb_cargo_information_id   "
			+ "    			WHERE inb.created_time >= ?1 AND inb.created_time <= ?2 AND decl.business_code = ?3 AND inb.is_cancel = 'F' AND inb.return_type = 'NONE' "
			+ "    			GROUP BY decl.declaration_number, hsc.hs_code_id,inb_barcode.barcode_id, to_char(inb.created_time, 'dd-MM-yyyy') "
			+ "				) inboundDetails "
			+ "				LEFT JOIN ( "
			+ "    				SELECT decl.declaration_number inbDeclarationNumber, to_char(inb.created_time, 'dd-MM-yyyy') createdDate,   "
			+ "    				(SELECT hs_code FROM hs_code WHERE hs_code_id = hsc.hs_code_id) hsCode, SUM(invoice.cif_value) totalCifValue "
			+ "    					FROM inbound inb "
			+ "        					LEFT JOIN inb_declaration_information decl ON inb.inbound_id = decl.inbound_id  "
			+ "        					LEFT JOIN inb_cargo_information cargo ON inb.inbound_id = cargo.inbound_id "
			+ "        					LEFT JOIN inb_invoice_details invoice ON cargo.id = invoice.inb_cargo_information_id  "
			+ "        					LEFT JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id "
			+ "    				WHERE inb.created_time >= ?1 AND inb.created_time <= ?2 AND decl.business_code = ?3 AND inb.is_cancel = 'F' AND inb.return_type = 'NONE' "
			+ "    				GROUP BY decl.declaration_number, hsc.hs_code_id, to_char(inb.created_time, 'dd-MM-yyyy') "
			+ "				) inbCifDetails "
			+ "				ON inbCifDetails.inbDeclarationNumber = inboundDetails.inbDeclarationNumber "
			+ "    			AND inbCifDetails.createdDate = inboundDetails.createdDate "
			+ "    			AND inbCifDetails.hsCode = inboundDetails.hsCode "
			+ "        ) inbdata FULL OUTER JOIN ( "
			+ "            	SELECT  "
			+ "					outboundDetails.inbDeclarationNumber, outboundDetails.createdDate, outboundDetails.hsCode,  "
			+ "					outboundDetails.itemDescription, outboundDetails.barcode, outboundDetails.barcodePiecesQuantity,  "
			+ "					outboundDetails.barcodePiecesNetWeight, outboundDetails.cargoPiecesQuantity, outboundDetails.cargoPiecesNetWeight,  "
			+ "					outboundCifDetails.totalCifValue FROM ( "
			+ "						SELECT outbDecl.inb_declaration_number inbDeclarationNumber,   "
			+ "						to_char(outb.created_time, 'dd-MM-yyyy') createdDate,  "
			+ "						(SELECT hs_code FROM hs_code WHERE hs_code_id = hsc.hs_code_id) hsCode,   "
			+ "						(SELECT hs_code_decription FROM hs_code WHERE hs_code_id = hsc.hs_code_id) itemDescription,   "
			+ "						(SELECT barcode FROM bar_code WHERE barcode_id = outb_barcode.barcode_id) barcode,   "
			+ "						SUM(outb_barcode.pieces_quantity) barcodePiecesQuantity,   "
			+ "						SUM(outb_barcode.pieces_net_weight) barcodePiecesNetWeight,   "
			+ "						SUM(cargo.pieces_quantity) cargoPiecesQuantity,   "
			+ "						SUM(cargo.pieces_net_weight) cargoPiecesNetWeight "
			+ "					FROM outbound outb "
			+ "						LEFT JOIN outb_declaration_info outbDecl ON outb.outbound_id = outbDecl.outbound_id   "
			+ "						LEFT JOIN outb_cargo_information cargo ON outb.outbound_id = cargo.outbound_id   "
			+ "						LEFT JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id  "
			+ "						LEFT JOIN outb_barcode_details outb_barcode ON cargo.id = outb_barcode.outb_cargo_information_id   "
			+ "					WHERE outb.created_time >= ?1 AND outb.created_time <= ?2 AND outbDecl.business_code = ?3 AND outb.is_cancel = 'F'   "
			+ "					GROUP BY outbDecl.inb_declaration_number, hsc.hs_code_id, outb_barcode.barcode_id, to_char(outb.created_time, 'dd-MM-yyyy')  "
			+ "			) outboundDetails "
			+ "			LEFT JOIN ( "
			+ "				SELECT outbDecl.inb_declaration_number inbDeclarationNumber, to_char(outb.created_time, 'dd-MM-yyyy') createdDate,   "
			+ "				(SELECT hs_code FROM hs_code WHERE hs_code_id = hsc.hs_code_id) hsCode, SUM(invoice.cif_value) totalCifValue "
			+ "				FROM outbound outb "
			+ "					LEFT JOIN outb_declaration_info outbDecl ON outb.outbound_id = outbDecl.outbound_id  "
			+ "					LEFT JOIN outb_cargo_information cargo ON outb.outbound_id = cargo.outbound_id "
			+ "					LEFT JOIN outb_invoice_details invoice ON cargo.id = invoice.outb_cargo_information_id  "
			+ "					LEFT JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id "
			+ "				WHERE outb.created_time >= ?1 AND outb.created_time <= ?2 AND outbDecl.business_code = ?3 AND outb.is_cancel = 'F'  "
			+ "				GROUP BY outbDecl.inb_declaration_number, hsc.hs_code_id, to_char(outb.created_time, 'dd-MM-yyyy') "
			+ "			) outboundCifDetails "
			+ "			ON outboundCifDetails.inbDeclarationNumber = outboundDetails.inbDeclarationNumber  "
			+ "				AND outboundCifDetails.createdDate = outboundDetails.createdDate "
			+ "				AND outboundCifDetails.hsCode = outboundDetails.hsCode "
			+ "        ) outbdata "
			+ "        ON inbdata.inbDeclarationNumber = outbdata.inbDeclarationNumber "
			+ "        AND inbdata.hsCode = outbdata.hsCode "
			+ "        AND ( CASE WHEN inbdata.barcode IS NULL THEN 'barcode_not_available' ELSE inbdata.barcode END ) = ( CASE WHEN outbdata.barcode IS NULL THEN 'barcode_not_available' ELSE outbdata.barcode END ) "
			+ "        AND inbdata.createdDate = outbdata.createdDate ORDER BY inbdata.createdDate DESC", nativeQuery = true)
	List<MismatchReportDataResponse> generateMismatchReport(Date fromDate, Date toDate, String businessCode);

	// Logic : Start1 <= End2 AND Start2 <= End1
	@Query("Select report FROM Report report WHERE report.fromDate <= ?2 AND report.toDate >= ?1 AND report.businessCode=?3 AND report.reportType = ?4")
	List<Report> findByDateRangeOverlapAndBusinessCodeAndReportType(Date fromDate, Date toDate, String businessCode, ReportType reportType);

	List<Report> findAllByReportType(ReportType reportType);
	
	// grouping HS code, goods_condition_id, inb_declaration_number, invoice_id level
	@Query(value = "SELECT invoicedata.reference_number invoiceReferenceNumber, invoicedata.created_time invoiceCreatedDate,"
			+ "	invoicedata.invoice_pages invoicePages, invoicedata.company_name consigneeName, invoicedata.currency currency, "
			+ "	invoicedata.grand_total grandTotal, "
			+ " outbdata.goods_condition goodsCondition, "
			+ "	outbdata.inb_declaration_number inbDeclarationNumber, outbdata.total_quantity totalQuantity, "
			+ "	outbdata.total_weight totalWeight, outbdata.total_cif_value cifValue,"
			+ " outbdata.hs_code_id hsCodeId "
			+ " FROM ( "
			+ "		SELECT outb.invoice_id invoice_id, "
			+ "		cargo.hs_code_id hs_code_id, "
			+ "		(SELECT goods_condition FROM goods_condition WHERE goods_condition_id = cargo.goods_condition_id) goods_condition,  "
			+ "		decl.inb_declaration_number, "
			+ "		SUM(cargo.pieces_quantity) total_quantity, "
			+ "		SUM(cargo.pieces_net_weight) total_weight, "
			+ "		SUM(invoice.cif_value) total_cif_value  "
			+ "		FROM outbound outb  "
			+ "			LEFT JOIN outb_cargo_information cargo ON outb.outbound_id = cargo.outbound_id "
			+ "			LEFT JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id "
			+ "			LEFT JOIN outb_invoice_details invoice ON cargo.id = invoice.outb_cargo_information_id "
			+ "			LEFT JOIN outb_declaration_info decl ON outb.outbound_id = decl.outbound_id "
			+ "		WHERE outb.invoice_id is not null "
			+ "		group by cargo.hs_code_id, cargo.goods_condition_id, decl.inb_declaration_number, outb.invoice_id) outbdata  "
			+ "	RIGHT JOIN "
			+ "		(SELECT invoice_detail_id, reference_number, created_time, invoice_pages,  "
			+ "		company_name, currency, grand_total FROM invoice_details "
			+ "		WHERE company_code= ?3 AND created_time >= ?1 AND created_time <= ?2) invoicedata "
			+ "	ON outbdata.invoice_id = invoicedata.invoice_detail_id", nativeQuery = true)
	List<GenericReportDataResponse> generateGenericReport(Date fromDate, Date toDate, String businessCode);
	
	
	// outbound > individual hscode, goods condition, inb_declaration_number level
	@Query(value = "SELECT invoicedata.reference_number invoiceReferenceNumber, invoicedata.created_time invoiceCreatedDate,"
			+ "	invoicedata.invoice_pages invoicePages, invoicedata.company_name consigneeName, invoicedata.currency currency, "
			+ "	invoicedata.grand_total grandTotal, "
			+ "	outbdata.hscode hsCode, outbdata.hscode_description hsCodeDescription, outbdata.goods_condition goodsCondition, "
			+ "	outbdata.inb_declaration_number inbDeclarationNumber, outbdata.total_quantity totalQuantity, "
			+ "	outbdata.total_weight totalWeight, outbdata.total_cif_value cifValue "
			+ " FROM ( "
			+ "		SELECT outb.invoice_id invoice_id, "
			+ "		(SELECT hs_code FROM hs_code WHERE hs_code_id = cargo.hs_code_id) hscode,  "
			+ "		(SELECT hs_code_decription FROM hs_code WHERE hs_code_id = cargo.hs_code_id) hscode_description,  "
			+ "		(SELECT goods_condition FROM goods_condition WHERE goods_condition_id = cargo.goods_condition_id) goods_condition,  "
			+ "		decl.inb_declaration_number, "
			+ "		cargo.pieces_quantity total_quantity, "
			+ "		cargo.pieces_net_weight total_weight, "
			+ "		invoice.cif_value total_cif_value  "
			+ "		FROM outbound outb  "
			+ "			JOIN outb_cargo_information cargo ON outb.outbound_id = cargo.outbound_id "
			+ "			JOIN hs_code hsc ON cargo.hs_code_id = hsc.hs_code_id "
			+ "			JOIN outb_invoice_details invoice ON cargo.id = invoice.outb_cargo_information_id "
			+ "			JOIN outb_declaration_info decl ON outb.outbound_id = decl.outbound_id "
			+ "		WHERE outb.invoice_id is not null) outbdata  "
			+ "	RIGHT JOIN "
			+ "		(SELECT invoice_detail_id, reference_number, created_time, invoice_pages,  "
			+ "		company_name, currency, grand_total FROM invoice_details  "
			+ "		WHERE company_code= ?3 AND created_time >= ?1 AND created_time <= ?2) invoicedata "
			+ "	ON outbdata.invoice_id = invoicedata.invoice_detail_id", nativeQuery = true)
	List<GenericReportDataResponse> generateGenericReport2(Date fromDate, Date toDate, String businessCode);

	@Query("SELECT r.reportId as reportId, r.createdOn as createdDate, (CASE WHEN r.referenceNumber IS NULL THEN ' ' ELSE r.referenceNumber END) as referenceNumber, r.reportType as reportType FROM Report r order by r.createdOn desc")
	List<ReportListResponse> list();

	@Query("SELECT r.reportId as reportId, r.createdOn as createdDate, (CASE WHEN r.referenceNumber IS NULL THEN ' ' ELSE r.referenceNumber END) as referenceNumber, r.reportType as reportType FROM Report r WHERE r.businessCode in (?1) order by r.createdOn desc")
	List<ReportListResponse> listByBusinessCodes(List<String> agentCodes);

}