
export enum charttype{
    Area='Area',
    Bar='Bar',
    Table='Table',
    Line='Line',
    Pie='Pie'
}

export const charttypeLabelMapping: Record<charttype, string> = {
    [charttype.Area]: "Area",
    [charttype.Bar]: "Bar",
    [charttype.Table]: "Table",
    [charttype.Line]: "Line",
    [charttype.Pie]: "Pie",
  };
  