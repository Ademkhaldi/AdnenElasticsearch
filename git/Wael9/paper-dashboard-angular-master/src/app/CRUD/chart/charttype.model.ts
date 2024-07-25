
export enum charttype{
    Area='Area',
    Line='Line',
    Pie='Pie'
}

export const charttypeLabelMapping: Record<charttype, string> = {
    [charttype.Area]: "Area",
    [charttype.Line]: "Line",
    [charttype.Pie]: "Pie",
  };
  